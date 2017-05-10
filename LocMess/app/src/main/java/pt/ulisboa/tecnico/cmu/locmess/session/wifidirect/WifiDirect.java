package pt.ulisboa.tecnico.cmu.locmess.session.wifidirect;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.GroupInfoListener;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.ulisboa.tecnico.cmu.locmess.session.LocMessService;


public class WifiDirect implements GroupInfoListener {

    public static WifiDirect singleton;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;

    private HashSet<String> nearbyDevices = null;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("WifiDirect","onServiceConnected...");
            mService = new Messenger(service);
            mManager = new SimWifiP2pManager(mService);
            mChannel = mManager.initialize(LocMessService.getContext(), Looper.getMainLooper(), null);

            new ReceivingTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("WifiDirect","onServiceDisconnected...");
            mService = null;
            mManager = null;
            mChannel = null;
        }
    };

    public WifiDirect(LocMessService service){

        SimWifiP2pSocketManager.Init(LocMessService.getContext());

        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        SimWifiP2pBroadcastReceiver receiver = new SimWifiP2pBroadcastReceiver();
        service.registerReceiver(receiver, filter);

        Intent intent = new Intent(LocMessService.getContext(), SimWifiP2pService.class);
        service.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList devices, SimWifiP2pInfo groupInfo) {

        if(nearbyDevices==null) nearbyDevices = new HashSet<>();
        HashSet<String> newDevices = new HashSet<>();

        for (String deviceName : groupInfo.getDevicesInNetwork()) {
            SimWifiP2pDevice device = devices.getByName(deviceName);
            if(device!=null) {
                String deviceIP = device.getVirtIp();
                newDevices.add(deviceIP);
                Log.d("WifiDirect","new device: "+deviceName+" "+deviceIP);
            }
        }
        for(String ip: newDevices) {
            if (!nearbyDevices.contains(ip)) {
                nearbyDevices.add(ip);
                sendData(ip);
            }
        }

        for(String ip: nearbyDevices)
            if(!newDevices.contains(ip))
                nearbyDevices.remove(ip);
    }

    public void sendData(String ip){
        String data = "Example message from: "+ip;
        new SendTask(ip).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,data);
    }

    public void getDevices(){
        mManager.requestGroupInfo(mChannel,this);
    }

}