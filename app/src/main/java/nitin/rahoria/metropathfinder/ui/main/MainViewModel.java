package nitin.rahoria.metropathfinder.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> _result = new MutableLiveData<>();
    LiveData<String> result = _result;
    private MutableLiveData<Integer> _distance = new MutableLiveData<>();
    LiveData<Integer> distance = _distance;


    int src, dest;
    int maxStations = 11;
    private ArrayList<ArrayList<Integer>> mAdjList = new ArrayList<>(maxStations);
    private String[] mStationArray;

    void initModel() {

        //initializing list
        for (int i = 0; i < maxStations; i++) {
            mAdjList.add(new ArrayList<>());
        }

        // populating list with adjacent stations
        addStations(0, 1);
        addStations(1, 5);
        addStations(1, 8);
        addStations(1, 2);
        addStations(2, 3);
        addStations(2, 6);
        addStations(3, 4);
        addStations(5, 7);
        addStations(6, 7);
        addStations(8, 9);
        addStations(9, 10);
    }

    // Method to add connected stations to list
    private void addStations(int i, int j) {
        mAdjList.get(i).add(j);
        mAdjList.get(j).add(i);
    }

    // Method to find shortest distance and path between source and destination
    void findShortestDistance() {
        if (src == dest) {
            _distance.postValue(0);
            return;
        }
        //predecessor array and distance array
        //predecessor array initialized with -1 (no predecessor)
        //and distance set to maximum value
        int pred[] = new int[maxStations];
        int dist[] = new int[maxStations];
        for (int i = 0; i < maxStations; i++) {
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        if (BFS(pred, dist) == false) {
            _distance.postValue(-1);
            return;
        }

        // LinkedList to store path
        LinkedList<Integer> path = new LinkedList<>();
        int crawl = dest;
        path.add(crawl);
        while (pred[crawl] != -1) {
            path.add(pred[crawl]);
            crawl = pred[crawl];
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Path: ");

        for (int i = path.size() - 1; i >= 0; i--) {
            sb.append(mStationArray[path.get(i)]);
            if(i>0)
                sb.append("  --->  ");
        }
        _distance.postValue(path.size());
        _result.postValue(sb.toString());
    }

    private boolean BFS(int pred[], int dist[])
    {
        // queue to track next connected vertices
        LinkedList<Integer> queue = new LinkedList<>();

        //array use to track if vertices is parsed or not
        //default will set as false
        boolean visited[] = new boolean[maxStations];
        for (int i = 0; i < maxStations; i++) {
            visited[i] = false;
        }

        visited[src] = true;
        dist[src] = 0;
        queue.add(src);

        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < mAdjList.get(u).size(); i++) {
                if (visited[mAdjList.get(u).get(i)] == false) {
                    visited[mAdjList.get(u).get(i)] = true;
                    dist[mAdjList.get(u).get(i)] = dist[u] + 1;
                    pred[mAdjList.get(u).get(i)] = u;
                    queue.add(mAdjList.get(u).get(i));

                    if (mAdjList.get(u).get(i) == dest)
                        return true;
                }
            }
        }
        return false;
    }

    public void setStationArray(String[] stringArray) {
        mStationArray = stringArray;
    }
}