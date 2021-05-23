package evhh.components;

import evhh.model.GameComponent;
import evhh.model.GameObject;

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
    public static final  int NOT = 0;
    public static final  int AND_2 = 0;
    public static final  int AND_3 = 0;
    public static final  int OR = 0;
    public static final  int XOR = 0;
    public static final  int NOR = 0;
    private ConnectorComponent connector;
    private long lastPacketId = 0;
    private int sources = 0;
    private final int logicalOperator;
    public BoolLogicComponent(GameObject parent, ConnectorComponent connector,int logicalOperator)
    {
        super(parent);
        this.logicalOperator = logicalOperator;

    }

    @Override
    public void onStart()
    {

    }

    @Override
    public void update()
    {

    }

    @Override
    public void onExit()
    {

    }
    public int onTransmit(int signal, long packetId, ConnectorComponent source)
    {
        if(lastPacketId == packetId)
            sources++;
        else
            sources=1;
        lastPacketId = packetId;
        //TODO FIX
        switch (logicalOperator){
            case NOT:return signal==0?1:0;
            default:return 0;
        }

    }
}
