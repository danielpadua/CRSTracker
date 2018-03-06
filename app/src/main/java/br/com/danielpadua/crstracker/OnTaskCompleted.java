package br.com.danielpadua.crstracker;

/**
 * Created by danielpadua on 15/02/2018.
 */

interface OnTaskCompleted {
    void onTaskCompleted();

    void onTaskCompleted(int[] widgetIds);

    void onTaskTimeout(int[] widgetIds);
}
