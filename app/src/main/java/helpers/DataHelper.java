package helpers;

public class  DataHelper {
    static Integer suhu = 0;
    static Integer ktanah = 0;
    static Integer lampu = 0;
    static Integer kipas = 0;
    static Integer kran = 0;
    static String title;
    static String message;

    public static String getTitle() {
        return title;
    }



    public static String getMessage() {
        return message;
    }



    public static Integer getLampu() {
        return lampu;
    }

    public static void setLampu(Integer lampu) {
        DataHelper.lampu = lampu;
    }

    public static Integer getKipas() {
        return kipas;
    }

    public static void setKipas(Integer kipas) {
        DataHelper.kipas = kipas;
    }

    public static Integer getKran() {
        return kran;
    }

    public static void setKran(Integer kran) {
        DataHelper.kran = kran;
    }



    public Integer getSuhu() {
        return suhu;
    }

    public void setSuhu(Integer suhu) {
        this.suhu = suhu;
    }

    public Integer getKtanah() {
        return ktanah;
    }

    public void setKtanah(Integer ktanah) {
        this.ktanah = ktanah;
    }

    public static void setTitle(String title) {
        DataHelper.title = title;
    }

    public static void setMessage(String message) {
        DataHelper.message = message;
    }
}
