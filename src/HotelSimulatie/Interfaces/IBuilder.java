package HotelSimulatie.Interfaces;

import java.util.ArrayList;

/**
 * IBuilder
 *
 * @param <B> the builder class interface, uses an array in the class.
 */
public interface IBuilder<B> {

    /**
     * AddNew
     *
     * @param obj for adding new Objects to the Array
     */
    void addNew(B obj);

    /**
     * GetArray
     *
     * @return returns the class Array
     */
    ArrayList<B> getArray();

    /**
     * ConstructNew
     *
     * @param s
     * @param i1
     * @param i2
     * @param i3
     * @param i4 The function the makes new object, it gets one string and four integers.
     */
    void constructNew(String s, int i1, int i2, int i3, int i4);

}
