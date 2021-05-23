package evhh.components;

/***********************************************************************************************************************
 * @project: AOOP_Sound_Board
 * @package: evhh.components
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-22
 * @time: 10:12
 **********************************************************************************************************************/
public interface Connectable
{
    public ConnectorComponent getConnector();
    public  void connect(ConnectorComponent connector);
    public void transmit(int signal,long packetId, ConnectorComponent source);
}
