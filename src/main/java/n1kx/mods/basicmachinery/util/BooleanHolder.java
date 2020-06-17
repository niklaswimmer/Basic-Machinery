package n1kx.mods.basicmachinery.util;

import n1kx.mods.basicmachinery.BasicMachinery;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;

/**
 * holds a HashMap with Strings as keys and booleans as values
 * @author N1Kx
 * @version 2020-06-16
 */
public class BooleanHolder {
    private final HashMap<String , Boolean> map;

    /**
     * if the arrays aren't the same length this constructor will create an empty map
     * @param strings   all keys this map should hold
     * @param bools     all values this map should hold
     */
    public BooleanHolder( String[] strings , boolean[] bools ) {
        this.map = new HashMap<>();

        if( bools.length == strings.length ) {
            for( int i = 0 ; i < strings.length ; i++ ) {
                this.map.put( strings[i] , bools[i] );
            }
        }
    }

    /**
     * sets the value of the given key
     * ATTENTION this method doesn't check if the key already exists or not
     * @param key   the key which value should change
     * @param value the new value
     */
    public void setValue( String key , boolean value ) {
        this.map.put( key , value );
    }

    /**
     * returns the value of the given key
     * @param key   the key which value should get return
     * @return      the value of the given key
     */
    public boolean getValue( String key ) {
        return this.map.get( key );
    }

}
