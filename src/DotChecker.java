public class DotChecker {

    public boolean isInsideArea(int x, float y, float r) {
        return isFirstQuarter(x, y, r) ||
                isSecondQuarter(x, y, r) ||
                isThirdQuarter(x, y, r) ||
                isFourthQuarter(x, y, r);
    }

    private boolean isFirstQuarter(int x, float y, float r) {
        if (x >= 0 && y >= 0) {
            return (y <= r / 2) && (y <= -x + r / 2);
        }
        return false;
    }

    private boolean isSecondQuarter(int x, float y, float r) {
        if (x <= 0 && y >= 0) {
            return (x * x + y * y <= r * r);
        }
        return false;
    }

    private boolean isThirdQuarter(int x, float y, float r) {
        return false;
    }

    private boolean isFourthQuarter(int x, float y, float r) {
        if (x >= 0 && y <= 0) {
            return (x <= r) && (y >= -r / 2); 
        }
        return false;
    }
}
