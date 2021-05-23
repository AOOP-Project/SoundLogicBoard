package evhh.components;

import evhh.model.GameComponent;
import evhh.model.GameObject;

import java.util.Arrays;

/***********************************************************************************************************************
 * @project: AOOP_Sound_Board
 * @package: evhh.components
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-22
 * @time: 10:10
 **********************************************************************************************************************/
public class BoolLogicComponent extends GameComponent
{
    public static final int NOT = 0;
    public static final int AND_2 = 1;
    public static final int AND_3 = 2;
    public static final int OR = 3;
    public static final int XOR = 4;
    public static final int NOR = 5;

    private ConnectorComponent connector;
    private final int logicalOperator;

    private long lastPacketId = 0;
    private long lastSentPacketId = 0;

    private int[] sources = {-1, -1, -1, -1};
    private int unTransmittedSignal = 0;
    private int storedSignal = 0;
    private int prevSignal = 0;
    private boolean delayedTransmit = false;

    public BoolLogicComponent(GameObject parent, ConnectorComponent connector, int logicalOperator)
    {
        super(parent);
        this.logicalOperator = logicalOperator;
        this.connector = connector;

    }

    @Override
    public void onStart()
    {

    }

    @Override
    public void update()
    {
        if(delayedTransmit)
        {
            if(unTransmittedSignal==-1)
                unTransmittedSignal = storedSignal;
            lastSentPacketId = ConnectorComponent.GeneratePacketId();
            for (int i = 0; i < sources.length; i++)
                if(sources[i]==-1)
                {
                    ConnectorComponent cc = connector.getConnected(i);
                    if(cc!=null)
                        if(connector.getIOAccess(i) == ConnectorComponent.SEND || connector.getIOAccess(i) == ConnectorComponent.SEND_RECEIVE)
                            if (cc.getIOAccess(i) == ConnectorComponent.SEND_RECEIVE || cc.getIOAccess(i) == ConnectorComponent.RECEIVE)
                            {
                                cc.transmit(unTransmittedSignal, lastSentPacketId, connector);
                            }
                }
            connector.setLastPacketId(lastPacketId);
            connector.setSignal(unTransmittedSignal);
            delayedTransmit = false;
            prevSignal = unTransmittedSignal;
            //sources = new int[]{-1, -1, -1, -1};
        }
    }

    @Override
    public void onExit()
    {

    }

    public void onTransmit(int signal, long packetId, ConnectorComponent source)
    {
        if (lastSentPacketId == packetId)
            return;
        int connection = connector.getConnection(source);
        sources[connection] = signal;
        int newSignal = -1;
        int on = 0;
        for (int n : sources)
            on += n != -1 ? n != 0 ? 1 : 0 : 0;
        switch (logicalOperator)
        {
            case NOT:
                newSignal = signal == 0 ? 1 : 0;
                break;
            case AND_2:
                newSignal = on >= 2 ? 1 : 0;
                break;
            case AND_3:
                newSignal = on >= 3 ? 1 : 0;
                break;
            case XOR:
                newSignal = on==1?1:0;
                break;
            case NOR:
                newSignal = on==0?1:0;
                break;
            default:
                break;
        }
        storedSignal = signal;
        unTransmittedSignal = newSignal;
        delayedTransmit = true;
    }
}
