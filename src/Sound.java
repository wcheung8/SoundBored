public class Sound {

    static int count;

    String name;
    int id;
    String keybind;

    public Sound(String n) {
        this.name = n;
        this.id = count;
        count++;
        if (HotKeyInterface.prop.containsKey(n)) {
            String keycode = HotKeyInterface.prop.getProperty(n);
            HotKeyInterface.registerHotkey(this, keycode);
        } else {
            this.keybind = "";
        }
    }


    @Override
    public String toString() {
        return name;
    }

    public void play() {
        Soundboard.playClip(this.name);
    }


}
