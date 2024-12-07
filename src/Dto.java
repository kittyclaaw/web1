import org.json.simple.JSONObject;

public class Dto {
    private int x;
    private float y;
    private float r;


    public int getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getR() {
        return r;
    }

    public void setAll (JSONObject jsonObject) throws ValidateException {
        try {
            this.x = Integer.parseInt(jsonObject.get("x").toString());
            this.y = Float.parseFloat(jsonObject.get("y").toString());
            this.r = Float.parseFloat(jsonObject.get("r").toString());
        } catch (NumberFormatException  e) {
            throw new ValidateException("Неверный формат (x, y, r)");
        }
    }

}