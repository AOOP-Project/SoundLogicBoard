package evhh.prefabs.logiccomponentsprefabs;

import evhh.components.BoolLogicComponent;
import evhh.components.ConnectorComponent;
import evhh.model.GameInstance;
import evhh.model.GameObject;
import evhh.model.Grid;
import evhh.model.ObjectPrefab;

import java.awt.image.BufferedImage;

/***********************************************************************************************************************
 * @project: AOOP_Sound_Board
 * @package: evhh.prefabs.logiccomponentsprefabs
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-23
 * @time: 13:56
 **********************************************************************************************************************/
public class BoolLogicGatePrefab extends ObjectPrefab
{
    transient private BufferedImage activeTexture;
    private String activeTextureRef;
    private int gateType;
    public BoolLogicGatePrefab(BufferedImage inactiveTexture,
                         String inactiveTextureRef,
                         BufferedImage activeTexture,
                         String activeTextureRef
            , int id,int gateType)
    {
        super(inactiveTexture, inactiveTextureRef,false, id);
        this.activeTexture = activeTexture;
        this.activeTextureRef = activeTextureRef;
        this.gateType = gateType;
    }
    @Override
    public GameObject getInstance(Grid grid, int x, int y)
    {
        GameObject instance = super.getInstance(grid, x, y);
        ConnectorComponent cc = new ConnectorComponent(instance,texture,textureRef,activeTexture,activeTextureRef);
        BoolLogicComponent blc = new BoolLogicComponent(instance,cc,gateType);
        cc.setLogicComponent(blc);
        instance.addComponent(cc);
        instance.addComponent(blc);
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
