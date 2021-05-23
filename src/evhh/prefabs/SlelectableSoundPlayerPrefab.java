package evhh.prefabs;

import evhh.common.assetloading.AssetLoader;
import evhh.components.ConnectorComponent;
import evhh.components.SoundPlayerComponent;
import evhh.model.GameInstance;
import evhh.model.GameObject;
import evhh.model.Grid;
import evhh.model.ObjectPrefab;
import evhh.model.gamecomponents.AudioComponent;
import evhh.view.audio.AudioListener;

import java.awt.image.BufferedImage;
import java.io.File;

/***********************************************************************************************************************
 * @project: AOOP_Sound_Board
 * @package: evhh.prefabs
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-23
 * @time: 14:52
 **********************************************************************************************************************/
public class SlelectableSoundPlayerPrefab extends ObjectPrefab
{

    private final AudioListener audioListener;
    transient private BufferedImage activeTexture;
    private String activeTextureRef;
    public SlelectableSoundPlayerPrefab(BufferedImage inactiveTexture,
                             String inactiveTextureRef,
                             BufferedImage activeTexture,
                             String activeTextureRef
            , int id, AudioListener audioListener)
    {
        super(inactiveTexture, inactiveTextureRef,false, id);
        this.activeTexture = activeTexture;
        this.activeTextureRef = activeTextureRef;
        this.audioListener = audioListener;
    }

    @Override
    public GameObject getInstance(Grid grid, int x, int y)
    {
        GameObject instance = super.getInstance(grid, x, y);
        ConnectorComponent cc = new ConnectorComponent(instance,texture,textureRef,activeTexture,activeTextureRef);
        File[] audioClips = new File[1];
        audioClips[0] = AssetLoader.loadFileUsingJFileChooser();
        if(audioClips[0]==null)
        {
            System.err.println("Invalid file path!");
            return null;
        }
        AudioComponent ac = new AudioComponent(instance,audioListener,audioClips);
        SoundPlayerComponent spc = new SoundPlayerComponent(instance,audioListener,ac,cc);
        instance.addComponent(cc);
        instance.addComponent(ac);
        instance.addComponent(spc);
        instance.setCreator(this);
        return instance;
    }

    @Override
    public boolean reloadTexture(GameInstance gameInstance)
    {
        super.reloadTexture(gameInstance);
        activeTexture = gameInstance.getTexture(activeTextureRef);
        return (texture !=null && activeTexture !=null);
    }
}
