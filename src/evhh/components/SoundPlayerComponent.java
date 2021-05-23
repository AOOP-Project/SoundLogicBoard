package evhh.components;

import evhh.model.GameComponent;
import evhh.model.GameObject;
import evhh.model.gamecomponents.AudioComponent;
import evhh.view.audio.AudioListener;

/***********************************************************************************************************************
 * @project: AOOP_Sound_Board
 * @package: evhh.components
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-22
 * @time: 10:09
 **********************************************************************************************************************/
public class SoundPlayerComponent extends GameComponent
{
    private AudioListener listener;
    private AudioComponent ac;
    private ConnectorComponent connector;
    private int selected = 0;

    public SoundPlayerComponent(GameObject parent, AudioListener listener, AudioComponent ac, ConnectorComponent connector)
    {
        super(parent);
        this.listener = listener;
        this.ac = ac;
        this.connector = connector;
    }

    @Override
    public void onStart()
    {

    }

    @Override
    public void update()
    {
        if(!ac.isPlaying()&&connector.isActive())
            ac.play(selected);
        //if(ac.isPlaying()&&!connector.isActive())
          //  ac.stop();

    }

    @Override
    public void onExit()
    {

    }
}
