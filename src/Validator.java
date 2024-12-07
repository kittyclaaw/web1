import org.json.simple.JSONObject;

public class Validator {

    public void validate(JSONObject jsonObject) throws ValidateException {
        Object xObj = jsonObject.get("x");
        Object yObj = jsonObject.get("y");
        Object rObj = jsonObject.get("r");

        String x = xObj != null ? xObj.toString() : null;
        String y = yObj != null ? yObj.toString() : null;
        String r = rObj != null ? rObj.toString() : null;

        if (x == null || x.isEmpty()) {
            throw new ValidateException("X не имеет значения");
        }
        try {
            int xx = Integer.parseInt(x);
            if (xx < -4 || xx > 4) {
                throw new ValidateException("Неверное значение X");
            }
        } catch (NumberFormatException e) {
            throw new ValidateException("X не является номером");
        }

        if (y == null || y.isEmpty()) {
            throw new ValidateException("Y не имеет значения");
        }
        try {
            float yy = Float.parseFloat(y);
            if (yy < -5 || yy > 5) {
                throw new ValidateException("Неверное значение Y");
            }
        } catch (NumberFormatException e) {
            throw new ValidateException("Y не является номером");
        }

        if (r == null || r.isEmpty()) {
            throw new ValidateException("R не имеет значения");
        }
        try {
            float rr = Float.parseFloat(r);
            if (rr < 1 || rr > 5) {
                throw new ValidateException("Неверное значение R");
            }
        } catch (NumberFormatException e) {
            throw new ValidateException("R не является номером");
        }
    }

}
