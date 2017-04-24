package k4n.eugenia.yandexapp.data.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Eugenia Kan on 16.04.2017.
 */

public class Languages {
    private List<String> dirs;
    private HashMap<String, String> langs;

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }

    public HashMap<String, String> getLangs() {
        return langs;
    }

    public void setLangs(HashMap<String, String> langs) {
        this.langs = langs;
    }
}
