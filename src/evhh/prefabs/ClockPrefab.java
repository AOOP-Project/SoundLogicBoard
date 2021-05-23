package evhh.prefabs;

import evhh.components.ClockComponent;
import evhh.components.ConnectorComponent;
import evhh.model.*;

import java.awt.image.BufferedImage;

/***********************************************************************************************************************
 * @project: AOOP_Sound_Board
 * @package: evhh.prefabs
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-22
 * @time: 10:01
 **********************************************************************************************************************/
public class ClockPrefab extends ObjectPrefab
{
    private final int frequency;
    transient private BufferedImage activeTexture;
    private String activeTextureRef;
    public ClockPrefab(BufferedImage inactiveTexture,
                       String inactiveTextureRef,
                       BufferedImage activeTexture,
                       String activeTextureRef
            , int id, int frequency)
    {
        super(inactiveTexture, inactiveTextureRef,false, id);
        this.activeTexture = activeTexture;
        this.activeTextureRef = activeTextureRef;
        this.frequency = frequency;
    }

    @Override
    public GameObject getInstance(Grid grid, int x, int y)
    {
        GameObject instance = super.getInstance(grid, x, y);
        ConnectorComponent cc = new ConnectorComponent(instance,texture,textureRef,activeTexture,activeTextureRef);
        instance.addComponent(cc);
        instance.addComponent(new ClockComponent(instance,cc,frequency ));
        cc.setIOAccess(ConnectorComponent.SEND,ConnectorComponent.NORTH);
        cc.setIOAccess(ConnectorComponent.SEND,ConnectorComponent.SOUTH);
        cc.setIOAccess(ConnectorComponent.SEND,ConnectorComponent.EAST);
        cc.setIOAccess(ConnectorComponent.SEND,ConnectorComponent.WEST);
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
