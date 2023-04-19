public class Dimension {
    private int w;
    private int h;

    public int getW() {
        return w;
    }
    public void setW(int w) {
        this.w = w;
    }
    public int getH() {
        return h;
    }
    public void setH(int h) {
        this.h = h;
    }

    public Dimension() {
        this.setW(0);
        this.setH(0);
    }

    public Dimension(int w, int h) {
        this.w = w;
        this.h = h;
    }

    @Override
    public String toString() {
        return "(" + this.w + ", " + this.h + ")";
    }
}



