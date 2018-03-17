import java.util.ArrayList;
import java.util.List;
import java.util.*;
/**
 * Client is used to connect to servers
 * A Client instance is initialized with an initial state
 * All reachable states are calculated upon instantiation
 * Server Ips need to be added manually with addServerIp(String)
 * run generateSolutions() to start looping through server ip's, creating threads,
 * and sending the initial state, goal state (one of allGoalStates),
 * and index to the server
 * 
 */
public class Client {

    private String initialState;
    private HashMap<Integer, String> solutions; // <index, solution>
    private List<String> allGoalStates;
    private List<String> serverIps;

    public Client(int[] is) {
        initialState = intArrayToString(is);
        solutions = new HashMap<Integer, String>();
        allGoalStates = new ArrayList<String>();
        serverIps = new ArrayList<String>();
        generateAllGoalStates();
    }
    public List<String> getAllGoalStates() {
        return allGoalStates;
    }

    public String getInitialStateString() {
        return initialState;
    }

    public int[] getInitialStateIntArray() {
        return stringToIntArray(initialState);
    }

    public void addServerIp(String ip) {
        serverIps.add(ip);
    }

    /**
     * Starts the process
     */
    public void generateSolutions() {
        if(serverIps.size() < 1) {
            System.out.println("No IPs Listed");
            return;
        }
        // need a thread for every server
        RequestThread[] threadArray = new RequestThread[serverIps.size()];
        for(int i = 0; i < serverIps.size(); i++) {
            threadArray[i] = new RequestThread(0,  initialState, "", solutions ); // (index, initialState, goalState, Solution)
        }
        // loop through every goal state, 
        int i = 0;
        while(i < allGoalStates.size()) {
            for(int j = 0; j < threadArray.length; j++) {
                if(!threadArray[j].isAlive()) {
                    threadArray[j] = new RequestThread(i, initialState, allGoalStates.get(i), solutions);
                    threadArray[j].start();
                    i++;
                }
            }
        }
    }

    private void generateAllGoalStates() {
        BoardState GoalState = new BoardState(initialState); 
        int[][] allPossibleStates = EPUtil.generateAllPossibleStates();
        for(int i = 0; i < allPossibleStates.length; i++) {
            if(EPUtil.checkReachable(allPossibleStates[i], GoalState.getCurrentState())) {
                allGoalStates.add(intArrayToString(allPossibleStates[i]));
            }
        }
    }

    public void printAllList() {
        for(int[] i : allGoalStates){
            for(int j : i) {
                System.out.print(j);
            }
            System.out.println();
        }
    }

    private String intArrayToString(int[] list) {
        String result = "";
        for(int i : list)
            result += i;
        return result;
    }

    private int[] stringToIntArray(String s) {
        int[] i = new int[s.length()];
        try{
            int j = 0;
            for(char c : s.toCharArray()) {
                i[j] = Character.getNumericValue(c);
                j++;
            }
        }catch(Exception e){return new int[]{-1};}
        return i;
    }

}

