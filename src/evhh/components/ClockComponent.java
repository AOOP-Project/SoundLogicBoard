package evhh.components;

import evhh.common.TimeReference;
import evhh.model.GameComponent;
import evhh.model.GameObject;

/***********************************************************************************************************************
 * @project: AOOP_Sound_Board
 * @package: evhh.components
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-22
 * @time: 10:09
 **********************************************************************************************************************/
public class ClockComponent extends GameComponent
{
    private ConnectorComponent conector;
    public int frequency;
    TimeReference timeReference;
    int signal = 0;

    public ClockComponent(GameObject parent, ConnectorComponent connector, int frequency)
    {
        super(parent);
        this.conector = connector;
        this.frequency = frequency;
        timeReference = new TimeReference();
        if(parent.getGrid().getGameInstance().isRunning())
            timeReference.start();
    }

    @Override
    public void onStart()
    {
        timeReference.start();
    }

    @Override
    public void update()
    {
        if((long)1000/frequency < timeReference.getDeltaTime())
        {
            timeReference.incrementStartTime(1000/frequency);
            toggle();
            transmit();
        }
    }

    private void transmit()
    {
        conector.transmit(signal,ConnectorComponent.GeneratePacketId(),null);
    }

    @Override
    public void onExit()
    {
    }
    private void toggle()
    {
        signal ^=  1; //This was probably my proudest moment programming
    }
}
