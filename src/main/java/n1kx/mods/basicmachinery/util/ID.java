package n1kx.mods.basicmachinery.util;

public class ID {

    private static int guiID = 0;

    public static int getNextGuiID() {
        return ID.guiID++;
    }

}
