package com.navin;

import android.util.Log;

import java.util.ArrayList;

public class PathFinder {

    Location starting_location, destination_location;

    PathFinder(Location A, Location B) {
        this.starting_location = A;
        this.destination_location = B;
        ArrayList<Location> loc = new ArrayList<Location>();
        loc.add(A);
        find(loc, destination_location, 0);
    }

    ArrayList<Location> final_path = new ArrayList<Location>();
    double lowest_metric = Double.POSITIVE_INFINITY;

    public void find(ArrayList<Location> visited_nodes, Location destination, double metric) {
        Location currentLocation = visited_nodes.get(visited_nodes.size()-1);
        Log.e("CURRENT LOCATION : ",currentLocation.toString());
        //visited_nodes.add(currentLocation);
        Log.e("SIZE OF VISITED NODE : ",String.valueOf(visited_nodes.size()));

        if(duplicate(currentLocation,visited_nodes)) {
            Log.e("RET","TRUE");
            return;
        }

        if(currentLocation.equals(destination)){
            Log.e("TEST","TEST");
            if(metric<lowest_metric){ lowest_metric = metric; final_path = visited_nodes;}
        }

        ArrayList<Location> nextHop = new ArrayList<Location>();

        for (Pair i : PinchZoomPan.main_mall.floors.get(0).roads) {
            if (i.A.equals(currentLocation)) {
                Log.e("T","T");
                nextHop.add(i.B);

            } else if (i.B.equals(currentLocation)) {
                Log.e("T","T");
                nextHop.add(i.A);
            }
        }

        Log.e("NEXT_HOPS",String.valueOf(nextHop.size()));

        for (int i = 0; i < nextHop.size(); i++) {
            ArrayList<Location> temp = new ArrayList<Location>();
            for(Location j : visited_nodes)
                temp.add(j);
            temp.add(nextHop.get(i));
            find(temp, destination, metric + 1);
        }
    }

    private boolean duplicate(Location m, ArrayList<Location> t){
        Log.e("frgrwqwegregr",String.valueOf(t.size()));
        if(t.size()==1)return false;
        for(int i = 0; i< t.size()-1;i++) {
            if (m.equals(t.get(i))) return true;
        }
        return false;
    }
}
