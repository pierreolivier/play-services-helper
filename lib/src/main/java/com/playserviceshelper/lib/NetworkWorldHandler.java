package com.playserviceshelper.lib;

import com.playserviceshelper.lib.listeners.HostElectionListeners;
import com.playserviceshelper.lib.listeners.NetworkEntityListeners;
import com.playserviceshelper.lib.messages.*;
import com.playserviceshelper.lib.utils.Logger;

import java.util.*;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class NetworkWorldHandler  implements NetworkEntityListeners {
    protected NetworkWorld mWorld;
    protected NetworkRoom mRoom;
    protected String mId;
    protected boolean mIsCreator;
    protected NetworkEntity mHost;
    protected boolean mIsHost;

    protected HostElectionListeners mHostElectionListeners;
    protected boolean mHostElection;
    final protected HashMap<NetworkEntity, List<Long>> mHostResults;
    protected boolean mHostResultSent;
    final protected HashMap<NetworkEntity, PingResultMessage> mHostPingResults;
    protected boolean mHostElected = false;

    public NetworkWorldHandler(NetworkWorld networkWorld) {
        super();

        mWorld = networkWorld;
        mRoom = null;
        mId = "";
        mIsCreator = false;

        mHostElectionListeners = null;
        mHostElection = false;
        mHostResults = new HashMap<NetworkEntity, List<Long>>();
        mHostResultSent = false;
        mHostPingResults = new HashMap<NetworkEntity, PingResultMessage>();
        mHostElected = false;
    }

    @Override
    public void onMessage(NetworkEntity entity, NetworkMessage message) {
        if (message instanceof PingMessage) {
            PingMessage pingMessage = (PingMessage) message;

            entity.sendUnreliableMessage(new PongMessage(entity.getId(), pingMessage.getTime()));
        } else if(message instanceof PongMessage) {
            PongMessage pongMessage = (PongMessage) message;
            long time = System.currentTimeMillis();

            mId = pongMessage.getParticipantId();
            mRoom.setIdPlayer(mId);
            if (mId.equals(mRoom.getCreator().getId())) {
                mIsCreator = true;
            }

            synchronized (mHostResults) {
                List<Long> results = mHostResults.get(entity);
                if (results == null) {
                    results = new ArrayList<Long>();
                    mHostResults.put(entity, results);
                }
                results.add(time - pongMessage.getTime());

                if (canSendHostResult()) {
                    sendHostResult();
                }
            }
        } else if (message instanceof PingResultMessage) {
            PingResultMessage pingResultMessage = (PingResultMessage) message;

            synchronized (mHostPingResults) {
                mHostPingResults.put(entity, pingResultMessage);

                Set<Map.Entry<NetworkEntity, PingResultMessage>> results = mHostPingResults.entrySet();
                if (results.size() == mRoom.getSize() - 1) {
                    long creatorResult = calculatePingResults();

                    NetworkEntity host = null;
                    long result = (creatorResult == 0 ? Long.MAX_VALUE : creatorResult);

                    for (Map.Entry<NetworkEntity, PingResultMessage> entry : results) {
                        if (entry.getValue().getResult() != 0 && entry.getValue().getResult() < result) {
                            host = entry.getKey();
                            result = entry.getValue().getResult();
                        }
                    }

                    if (host == null) {
                        mIsHost = true;

                        mRoom.broadcastReliableMessage(new NewHostMessage(mId));

                        Logger.e("network creator hosting", "host: " + mId);
                    } else {
                        mIsHost = false;
                        mHost = host;

                        mRoom.broadcastReliableMessage(new NewHostMessage(host.getId()));

                        Logger.e("network creator n-hosting", "host: " + host.getId());
                    }

                    mHostElected = true;
                    mHostElection = false;
                    if (mHostElectionListeners != null) {
                        mHostElectionListeners.onHostElected(mIsHost);
                    }
                }
            }
        } else if (message instanceof NewHostMessage) {
            NewHostMessage newHostMessage = (NewHostMessage) message;

            Logger.e("network", "host: " + newHostMessage.getHostId());

            if (newHostMessage.getHostId().equals(mId)) {
                mIsHost = true;
            } else {
                mIsHost = false;
                mHost = mRoom.getEntity(newHostMessage.getHostId());
            }

            mHostElected = true;
            mHostElection = false;
            if (mHostElectionListeners != null) {
                mHostElectionListeners.onHostElected(mIsHost);
            }
        }
    }

    public NetworkRoom getRoom() {
        return mRoom;
    }

    public void setRoom(NetworkRoom mRoom) {
        this.mRoom = mRoom;
    }

    public void startHostElection(HostElectionListeners hostElectionListeners) {
        if (mHost == null && !mHostElection) {
            mHostElectionListeners = hostElectionListeners;
            mHostElection = true;

            // Reset host election vars
            mHostResults.clear();
            mHostResultSent = false;
            mHostPingResults.clear();
            mHostElected = false;

            for (int i = 0 ; i < 10 ; i++) {
                PingMessage pingMessage = new PingMessage(System.currentTimeMillis());
                mRoom.broadcastUnreliableMessage(pingMessage);
            }

            new Thread() {
                public void run() {
                    try {
                        sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (mHostResults) {
                        if (!mHostResultSent && !mIsCreator) {
                            Logger.e("network", "timeout ping");
                            sendHostResult();
                        }
                    }
                    try {
                        sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!mHostElected) {
                        mHost = null;
                        mHostElection = false;

                        if (mHostElectionListeners != null) {
                            mHostElectionListeners.onHostElectionFailed();
                        }
                    }
                }
            }.start();
        }
    }

    private boolean canSendHostResult() {
        if (mHostResultSent || mIsCreator) {
            return false;
        }

        for (List<Long> results : mHostResults.values()) {
            if (results.size() < 10) {
                return false;
            }
        }

        return true;
    }

    private void sendHostResult() {
        mHostResultSent = true;

        mRoom.getCreator().sendReliableMessage(new PingResultMessage(mHostResults));
    }

    private long calculatePingResults() {
        long result = 0;

        for (List<Long> values : mHostResults.values()) {
            long subResult = 0;
            for (Long value : values) {
                subResult += value;
            }
            subResult /= values.size();
            result += subResult;
        }

        return result;
    }
}
