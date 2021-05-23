package evhh.components;

import evhh.model.GameComponent;
import evhh.model.GameObject;

import java.awt.image.BufferedImage;

/***********************************************************************************************************************
 * @project: AOOP_Sound_Board
 * @package: evhh.components
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-22
 * @time: 10:08
 **********************************************************************************************************************/
public class ConnectorComponent extends GameComponent implements Connectable
{
    public static final int SEND_RECEIVE = 0;
    public static final int RECEIVE = 1;
    public static final int SEND = 2;
    public static final int BLOCKED = 3;

    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;


    private int[] ioAccess = {SEND_RECEIVE, SEND_RECEIVE, SEND_RECEIVE, SEND_RECEIVE};
    private ConnectorComponent[] connections = new ConnectorComponent[4];
    private long lastPacketId = 0;
    BufferedImage inactiveTexture;
    String inactiveTextureRef;
    BufferedImage activeTexture;
    String activeTextureRef;
    private BoolLogicComponent logicComponent;

    private int signal = 0;

    public ConnectorComponent(GameObject parent, BufferedImage inactiveTexture, String inactiveTextureRef, BufferedImage activeTexture, String activeTextureRef)
    {
        super(parent);
        if (parent.getGrid() == null)
            return;
        this.inactiveTexture = inactiveTexture;
        this.inactiveTextureRef = inactiveTextureRef;
        this.activeTexture = activeTexture;
        this.activeTextureRef = activeTextureRef;
        GameObject[] adjacent = new GameObject[4];
        if (parent.getGrid().isValidCoordinates(getX() - 1, getY()) && !parent.getGrid().isEmpty(getX() - 1, getY()))
            adjacent[ConnectorComponent.NORTH] = parent.getGrid().get(getX() - 1, getY());
        if (parent.getGrid().isValidCoordinates(getX() + 1, getY()) && !parent.getGrid().isEmpty(getX() + 1, getY()))
            adjacent[ConnectorComponent.SOUTH] = parent.getGrid().get(getX() + 1, getY());
        if (parent.getGrid().isValidCoordinates(getX(), getY() - 1) && !parent.getGrid().isEmpty(getX(), getY() - 1))
            adjacent[ConnectorComponent.WEST] = parent.getGrid().get(getX(), getY() - 1);
        if (parent.getGrid().isValidCoordinates(getX(), getY() + 1) && !parent.getGrid().isEmpty(getX(), getY() + 1))
            adjacent[ConnectorComponent.EAST] = parent.getGrid().get(getX(), getY() + 1);
        for (GameObject go : adjacent)
            if(go!=null)
                for (GameComponent gc : go.getComponentList())
                    if (gc instanceof Connectable)
                    {
                        ((Connectable) gc).connect(this);
                        connect(((Connectable) gc).getConnector());
                    }

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

    @Override
    public ConnectorComponent getConnector()
    {
        return this;
    }

    @Override
    public void connect(ConnectorComponent connector)
    {
        assert connector != null;
        int x = getX() - connector.getX();
        int y = getY() - connector.getY();
        if (x != 0)
            connections[(x > 0) ? ConnectorComponent.WEST : ConnectorComponent.EAST] = connector;
        else if (y != 0)
            connections[(y > 0) ? ConnectorComponent.SOUTH : ConnectorComponent.NORTH] = connector;
    }

    @Override
    public void transmit(int signal, long packetId, ConnectorComponent source)
    {
        if(logicComponent!=null)
        {
            logicComponent.onTransmit(signal,packetId,source);
            return;
        }
        if (lastPacketId == packetId)
            return;
        if (signal != 0 && this.signal == 0)
            parent.getSprite().switchImage(activeTexture, activeTextureRef);
        else if (signal == 0 && this.signal != 0)
            parent.getSprite().switchImage(inactiveTexture, inactiveTextureRef);
        this.signal = signal;
        lastPacketId = packetId;
        for (int i = 0; i < connections.length; i++)
            if (connections[i] != null && source != connections[i])
                if (ioAccess[i] == ConnectorComponent.SEND || ioAccess[i] == ConnectorComponent.SEND_RECEIVE)
                    if (connections[i].getIOAccess(i) == ConnectorComponent.SEND_RECEIVE || connections[i].getIOAccess(i) == ConnectorComponent.RECEIVE)
                    {
                        //System.out.println(i + ", " + signal + ", " + packetId);
                        connections[i].transmit(signal, packetId, this);
                    }
    }

    public void setIOAccess(int access, int connection)
    {
        assert (ioAccess.length >= connection && 0 < connection);
        assert access <= BLOCKED && access >= 0;
        ioAccess[connection] = access;
    }

    public int getIOAccess(int connection)
    {
        assert (ioAccess.length >= connection && 0 < connection);
        return ioAccess[connection];
    }

    public static long GeneratePacketId()
    {
        return (long) (Math.random() * Long.MAX_VALUE);
    }

    public BoolLogicComponent getLogicComponent()
    {
        return logicComponent;
    }

    public void setLogicComponent(BoolLogicComponent logicComponent)
    {
        this.logicComponent = logicComponent;
    }
    public ConnectorComponent getConnected(int connection)
    {
        assert (connections.length >= connection && 0 < connection);
        return connections[connection];
    }
    public int getConnection(ConnectorComponent connector)
    {
        for (int i = 0; i <connections.length ; i++)
        {
            if(connections[i]==connector)
                return i;
        }
        return -1;
    }
    public void setSignal(int signal)
    {
        if(signal == -1)
            return;
        this.signal = signal;
    }

    public void setLastPacketId(long lastPacketId)
    {
        this.lastPacketId = lastPacketId;
    }
    public boolean isActive()
    {
        return signal!=0;
    }
}
