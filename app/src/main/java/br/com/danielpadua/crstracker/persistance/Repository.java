package br.com.danielpadua.crstracker.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.danielpadua.crstracker.model.pair.Pair;
import br.com.danielpadua.crstracker.model.widget.WidgetTracker;

import static br.com.danielpadua.crstracker.util.CRSUtil.GLOBAL_CRS_SHARED_PREFERENCES_NAME;

/**
 * Created by danielpadua on 20/03/2018.
 */

public class Repository<T> {

    private final SharedPreferences crsPrefs;
    private final Gson gson;

    private final Class<T> type;

    public Repository(Context context, Class<T> type) {
        this.crsPrefs = context.getSharedPreferences(GLOBAL_CRS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.type = type;
        this.gson = new GsonBuilder().registerTypeAdapter(Pair.class, new PairAdapter())
                .registerTypeAdapter(WidgetTracker.class, new WidgetTrackerAdapter()).create();
    }

    public void persist(String id, T entity) {
        this.crsPrefs.edit().putString(id, this.gson.toJson(entity)).apply();
    }

    public void remove(String id) {
        this.crsPrefs.edit().remove(id).apply();
    }

    public T get(String id) {
        return this.gson.fromJson(this.crsPrefs.getString(id, null), this.type);
    }

    public List<T> getAll() {
        List<T> entities = new ArrayList<>();
        Map<String, ?> map = this.crsPrefs.getAll();
        Iterator it1 = map.entrySet().iterator();

        while (it1.hasNext()) {
            Map.Entry pair = (Map.Entry) it1.next();
            T entity = this.gson.fromJson(pair.getValue().toString(), this.type);
            if (entity != null) {
                entities.add(entity);
            }
            it1.remove();
        }
        return entities;
    }
}
