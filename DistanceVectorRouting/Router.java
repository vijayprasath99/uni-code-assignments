
package DistanceVectorRouting;

import java.io.Serializable;


public class Router implements Serializable{
    
   private int[] r0 = new int[5];
   private int[] r1 = new int[5];
   private int[] r2 = new int[5];
   private int[] r3 = new int[5];
   private int trno;

    /**
     * @return the r0
     */
    public int[] getR0() {
        return r0;
    }

    /**
     * @param r0 the r0 to set
     */
    public void setR0(int[] r0) {
        this.r0 = r0;
    }

    /**
     * @return the r1
     */
    public int[] getR1() {
        return r1;
    }

    /**
     * @param r1 the r1 to set
     */
    public void setR1(int[] r1) {
        this.r1 = r1;
    }

    /**
     * @return the r2
     */
    public int[] getR2() {
        return r2;
    }

    /**
     * @param r2 the r2 to set
     */
    public void setR2(int[] r2) {
        this.r2 = r2;
    }

    /**
     * @return the r3
     */
    public int[] getR3() {
        return r3;
    }

    /**
     * @param r3 the r3 to set
     */
    public void setR3(int[] r3) {
        this.r3 = r3;
    }

    /**
     * @return the trno
     */
    public int getTrno() {
        return trno;
    }

    /**
     * @param trno the trno to set
     */
    public void setTrno(int trno) {
        this.trno = trno;
    }
   
   
    
}
