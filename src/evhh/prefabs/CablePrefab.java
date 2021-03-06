package evhh.prefabs;

import evhh.components.ConnectorComponent;
import evhh.model.GameInstance;
import evhh.model.GameObject;
import evhh.model.Grid;
import evhh.model.ObjectPrefab;

import java.awt.image.BufferedImage;

/***********************************************************************************************************************
 * @project: AOOP_Sound_Board
 * @package: evhh.prefabs
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-22
 * @time: 10:01
 **********************************************************************************************************************/
public class CablePrefab extends ObjectPrefab
{
    public static final int FOUR_WAY = 0;
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;

    transient private BufferedImage activeTexture;
    private String activeTextureRef;
    private int cableType;

    public CablePrefab(BufferedImage inactiveTexture,
                       String inactiveTextureRef,
                       BufferedImage activeTexture,
                       String activeTextureRef
            , int id, int cableType)
    {
        super(inactiveTexture, inactiveTextureRef, false, id);
        this.activeTexture = activeTexture;
        this.activeTextureRef = activeTextureRef;
        this.cableType = cableType;
    }

    @Override
    public GameObject getInstance(Grid grid, int x, int y)
    {
        GameObject instance = super.getInstance(grid, x, y);
        ConnectorComponent cc = new ConnectorComponent(instance, texture, textureRef, activeTexture, activeTextureRef);
        instance.addComponent(cc);
        instance.setCreator(this);
        if (cableType == VERTICAL)
        {
            cc.setIOAccess(ConnectorComponent.BLOCKED, ConnectorComponent.EAST);
            cc.setIOAccess(ConnectorComponent.BLOCKED, ConnectorComponent.WEST);
        } else if (cableType == HORIZONTAL)
        {
            cc.setIOAccess(ConnectorComponent.BLOCKED, ConnectorComponent.NORTH);
            cc.setIOAccess(ConnectorComponent.BLOCKED, ConnectorComponent.SOUTH);
        }

        return instance;
    }

    @Override
    public boolean reloadTexture(GameInstance gameInstance)
    {
        super.reloadTexture(gameInstance);
        activeTexture = gameInstance.getTexture(activeTextureRef);
        return (texture != null && activeTexture != null);
    }
}
