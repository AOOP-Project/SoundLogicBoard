package evhh;

import evhh.common.imagemanipulation.ImageTiler;
import evhh.components.BoolLogicComponent;
import evhh.controller.InputManager.UserInputManager;
import evhh.model.EventTrigger;
import evhh.model.GameInstance;
import evhh.model.Grid;
import evhh.model.ObjectPrefab;
import evhh.model.mapeditor.MapEditor;
import evhh.prefabs.CablePrefab;
import evhh.prefabs.ClockPrefab;
import evhh.prefabs.logiccomponentsprefabs.AndGatePrefab;
import evhh.prefabs.logiccomponentsprefabs.BoolLogicGatePrefab;
import evhh.prefabs.logiccomponentsprefabs.NotGatePrefab;
import evhh.prefabs.logiccomponentsprefabs.OrGatePrefab;
import evhh.view.audio.AudioListener;
import evhh.view.renderers.FrameRenderer;
import evhh.view.renderers.GameFrame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/***********************************************************************************************************************
 * @project: AOOP_Sound_Board
 * @package: evhh
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-22
 * @time: 09:48
 **********************************************************************************************************************/
public class Main
{


    public static final int DEFAULT_GRID_WIDTH = 16;
    public static final int DEFAULT_GRID_HEIGHT = 16;
    public static final int DEFAULT_CELL_SIZE = 32;
    public static final String GRID_SAVE_PATH = System.getProperty("user.dir") + "\\SavedData\\SavedGrids\\";
    public static final String ASSET_PATH = System.getProperty("user.dir") + "\\Assets";
    public static final int TIMER_DELAY = 1000 / 60;

    private static GameInstance game1;
    private static Grid grid;
    private static FrameRenderer frameRenderer;
    private static GameFrame gameFrame;
    private static UserInputManager userInputManager;
    private static AudioListener audioListener;

    private static MapEditor mapEditor;
    private static EventTrigger levelSwitchingEvent;
    private static boolean runWithMapEditor;

    private static CablePrefab fourWayCablePrefab;
    private static CablePrefab horizontalCablePrefab;
    private static CablePrefab verticalCablePrefab;
    private static ClockPrefab clockPrefab;
    private static ClockPrefab clockPrefab2;
    private static NotGatePrefab notPrefab;
    private static AndGatePrefab andPrefab;
    private static OrGatePrefab orPrefab;
    private static BoolLogicGatePrefab xorPrefab;

    public static void main(String[] args)
    {
        createGame();
        runWithMapEditor();
        game1.start();

    }

    private static void createGame()
    {
        game1 = new GameInstance("Game1");
        grid = new Grid(DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT);
        gameFrame = new GameFrame(DEFAULT_GRID_WIDTH * DEFAULT_CELL_SIZE, DEFAULT_GRID_HEIGHT * DEFAULT_CELL_SIZE, "Game1");
        frameRenderer = new FrameRenderer(gameFrame, DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT, DEFAULT_CELL_SIZE);
        userInputManager = new UserInputManager(game1);
        game1.setMainGrid(grid);
        game1.setFrameRenderer(frameRenderer);
        game1.setUserInputManager(userInputManager);
        game1.addRendererTimer(TIMER_DELAY);
        game1.setUpdateTimer(TIMER_DELAY);
        game1.loadTextureAssets(ASSET_PATH + "\\Textures");
        game1.getFrameRenderer().setGridBackgroundImage(ImageTiler.tileImage(game1.getTexture("blank"), DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT));
        audioListener = new AudioListener();
        game1.getFrameRenderer().setAudioListener(audioListener);


        BufferedImage tiled = ImageTiler.tileImage(game1.getTexture("blank"), DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT);
        try
        {
            ImageIO.write(tiled, "png", new File(ASSET_PATH + "/tiled.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    private static void runWithMapEditor()
    {
        Thread thread = new Thread(() -> buildScene());
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {

                synchronized (game1)
                {
                    game1.refreshSpritesInRenderer();
                    game1.refreshMappedUserInput();

                    if (mapEditor != null)
                        if (mapEditor.getWorkingGrid() != game1.getMainGrid())
                        {
                            game1.setMainGrid(mapEditor.getWorkingGrid());
                        }
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,0,100);
        thread.start();
    }
    private static void buildScene()
    {
        fourWayCablePrefab = new CablePrefab(game1.getTexture("CableInactive"),
                "CableInactive",
                game1.getTexture("CableActive"),
                "CableActive",
                110,CablePrefab.FOUR_WAY);
        verticalCablePrefab = new CablePrefab(game1.getTexture("VertCableInactive"),
                "VertCableInactive",
                game1.getTexture("VertCableActive"),
                "VertCableActive",
                110,CablePrefab.VERTICAL);
        horizontalCablePrefab = new CablePrefab(game1.getTexture("HorCableInactive"),
                "HorCableInactive",
                game1.getTexture("HorCableActive"),
                "HorCableActive",
                110,CablePrefab.HORIZONTAL);
        clockPrefab = new ClockPrefab(game1.getTexture("ClockInactive"),
                "ClockInactive",
                game1.getTexture("ClockActive"),
                "ClockActive",
                120,2);
        clockPrefab2 = new ClockPrefab(game1.getTexture("ClockInactive"),
                "ClockInactive",
                game1.getTexture("ClockActive"),
                "ClockActive",
                120,4);
        notPrefab = new NotGatePrefab(game1.getTexture("NotInactive"),
                "NotInactive",
                game1.getTexture("NotActive"),
                "NotActive",
                120);
        andPrefab = new AndGatePrefab(game1.getTexture("AndInactive"),
                "AndInactive",
                game1.getTexture("AndActive"),
                "AndActive",
                120);
        orPrefab = new OrGatePrefab(game1.getTexture("OrInactive"),
                "OrInactive",
                game1.getTexture("OrActive"),
                "OrActive",
                120);
        xorPrefab = new BoolLogicGatePrefab(game1.getTexture("XorInactive"),
                "XorInactive",
                game1.getTexture("XorActive"),
                "XorActive",
                120,BoolLogicComponent.XOR);


        mapEditor = new MapEditor(grid,DEFAULT_CELL_SIZE,new ObjectPrefab[]{
                fourWayCablePrefab,
                verticalCablePrefab,
                horizontalCablePrefab,
                clockPrefab,
                clockPrefab2,
                notPrefab,
                andPrefab,
                orPrefab,
                xorPrefab
        });
    }
}