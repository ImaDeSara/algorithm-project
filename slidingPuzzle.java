import java.io.*;
import java.util.Scanner;

public class slidingPuzzle {

    public static int height, width;
    public static String[][] mainArray;
    public static int[][] visitedArray, substituteArray, pathArray, completePathArray;
    public static boolean foundStartingDirection = false;
    public static int methodGroup;
    public static int substituteArrayInteger = 0, visitedArrayInteger = 0, pathArrayInteger = 0;
    public static int distance = 0; // Initialize the distance counter
    public static int distance1 = 0, distance2 = 0;
    public static int currentInteger;
    public static int leftPositionNum, rightPositionNum, upPositionNum, downPositionNum;
    public static int startRow = -1, startCol = -1, finishCol = -1, finishRow = -1;
    public static boolean leftMain = false, rightMain = false, upMain = false, downMain = false;
    public static boolean found_aPath = false, updatedPath = false;
    public static boolean got_a_complete_path = false;// this get true when some path that finds "F" and put into complete array

    public static boolean nodeFound = false; //checks the node is in substituteArray & visitedArray
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Specify the folder path
        String folderPath = "G:\\Year 2\\semester 2\\Algorithms Theory, Design and Implementation\\coursework\\Algorithm Coursework\\benchmark_series";
        // List files in the folder
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        // Display available files
        System.out.println("Available files:");
        for (File file : files) {
            System.out.println(file.getName());
        }
        // Prompt user for the file they need
        System.out.println("Enter the name of the file you need (including extension):");
        String fileName = scanner.nextLine();
        String filePath = folderPath + File.separator + fileName;

        // Read the content of the selected file
        try {
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file))); //read characters from the specified file
            String line; // store each line of text as it's read from the file
            // StringBuilder is a class in Java that allows to efficiently construct and manipulate strings
            StringBuilder content = new StringBuilder(); // creates a new StringBuilder object named 'content'
            // Inside the while loop, we read the next line of text from the file and assign it to the 'line' variable
            // The condition 'reader.readLine() != null' ensures that we continue looping until there are no more lines left to read
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n"); // Append the current line of text to the 'content' StringBuilder
            }
            reader.close();

            String[] lines = content.toString().split("\n"); // Split the content of the file into individual arrays
            int rows = lines.length; //number of rows in the graph
            height = rows;
            int columns = lines[0].length(); //number of columns in the graph
            width = columns;
            mainArray = new String[rows][columns]; //2D array to store the graph
            // Iterate through each line to populate the mainArray
            for (int i = 0; i < rows; i++) { //traverse through that line array to find S
                //split each line into an array named characters
                String[] characters = lines[i].split("");
                for (int j = 0; j < columns; j++) { // Iterate over each character in the line
                    mainArray[i][j] = characters[j];
                    // Check if the current string( characters[j] ) in the characters array contains the character 'S'
                    if (mainArray[i][j].equals("S")) { //if (characters[j].indexOf('S') != -1)
                        startCol = j;
                        startRow = i;
                    } else if (mainArray[i][j].equals("F")) { //if (characters[j].indexOf('F') != -1)
                        finishCol = j;
                        finishRow = i;
                    }
                }
            }

            substituteArray = new int[rows*columns][5]; //creating a substitute array to store coordinates of possible paths other than the main selected one
            visitedArray = new int[rows*columns][2]; //creating a visited array to store coordinates of the nodes that are visited already
            pathArray = new int[rows*columns][4]; //creates a path to traverse
            completePathArray = new int[rows*columns][4];

            for (int i = 0; i < substituteArray.length; i++) {
                for (int j = 0; j < substituteArray[i].length; j++) {
                    if (substituteArray[i][j] == 0) {
                        substituteArray[i][j] = -1; //initialize every sub array(coordinate) to -1 because if it's accidentally 0, and it will get conflicted because there is a index as 0 as well
                    }
                }
            }
            for (int i = 0; i < visitedArray.length; i++) {
                for (int j = 0; j < visitedArray[i].length; j++) {
                    if (visitedArray[i][j] == 0) {
                        visitedArray[i][j] = -1; //initialize every sub array(coordinate) to -1 because if it's accidentally 0, and it will get conflicted because there is a index as 0 as well
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        scanner.close();
        Long startTime = System.nanoTime();
        Begin(startRow, startCol);
        Long endTime = System.nanoTime();
        Long timeDuration = (endTime - startTime)/1000000; //time in milliseconds
        System.out.println("Time duration: " + timeDuration + "ms");
    }

    public static void Begin(int startRow, int startCol) {
        visitedArray[visitedArrayInteger][0] = startRow;
        visitedArray[visitedArrayInteger][1] = startCol;
        visitedArrayInteger++;

        pathArray[pathArrayInteger][0] = startRow;
        pathArray[pathArrayInteger][1]  = startCol;
        pathArray[pathArrayInteger][2] = distance;
        pathArray[pathArrayInteger][3] = pathArrayInteger;
        pathArrayInteger++;

        int methodNumber = 0;
        if ( mainArray[startRow][startCol-1].equals(".")) {
            foundStartingDirection = true;
            methodNumber = 1;
            leftMain=true;
        } if (mainArray[startRow+1][startCol].equals(".")) {
            distance=0;
            if (!foundStartingDirection) { //checks if foundPath is false (foundpath==false)
                foundStartingDirection = true; //here we get foundStartingDirection so, we won't check both left and down paths to run, we need to run only one path, that's why we use a foundPath variable here
                downMain=true;
                methodGroup = 2;
            }
            else { //foundPath = true
                for (int i = startRow+1; i <= height-1; i++) {
                    if ( mainArray[i][startCol].equals(".") ) { // we check the distance of how far we can go down
                        distance++; //calculate the distance until meeting a rock or F, but don't visit them
                        downPositionNum=i;
                    } else {
                        break;
                    }
                }
                substituteArray[substituteArrayInteger][0] = downPositionNum;
                substituteArray[substituteArrayInteger][1] = startCol;
                substituteArray[substituteArrayInteger][2] = distance;
                substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                substituteArray[substituteArrayInteger][4] = 1;
                substituteArrayInteger++;
            }
        } if ( mainArray[startRow][startCol+1].equals(".") ) {
            distance=0;
            if (!foundStartingDirection) { //checks if foundPath is false
                foundStartingDirection = true;
                methodNumber = 3;
                rightMain=true;
            } else { //foundPath = true
                for (int i = startCol+1; i < width; i++) {
                    if ( mainArray[startRow][i].equals(".") ) {
                        distance++;
                        rightPositionNum=i;
                    } else {
                        break;
                    }
                }
                substituteArray[substituteArrayInteger][0] = startRow;
                substituteArray[substituteArrayInteger][1] = rightPositionNum;
                substituteArray[substituteArrayInteger][2] = distance;
                substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                substituteArray[substituteArrayInteger][4] = 2;
                substituteArrayInteger++;
            }
        } if (mainArray[startRow-1][startCol].equals(".")) {
            distance=0;
            if (!foundStartingDirection) { //checks if foundPath is false
                foundStartingDirection = true;
                methodNumber = 4;
                upMain=true;
            }
            else {
                for (int i = startRow-1; i >= 0; i--) {
                    if ( mainArray[i][startCol].equals(".") ) {
                        distance++;
                        upPositionNum=i;
                    } else {
                        break;
                    }
                }
                substituteArray[substituteArrayInteger][0] = upPositionNum;
                substituteArray[substituteArrayInteger][1] = startCol;
                substituteArray[substituteArrayInteger][2] = distance;
                substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                substituteArray[substituteArrayInteger][4] = 1;
                substituteArrayInteger++;
            }
        }
        distance = 0; // distance = 0, because in the above scenario, the distance got updated, so here we only want to find a direction to start traversing, and when we start. it should start from 0, those updated distance values sent to the substitute array.
        if (methodNumber==1){
            leftMethod(startRow, startCol, distance);
        } else if (methodNumber==2) {
            downMethod(startRow, startCol, distance);
        } else if (methodNumber==3) {
            rightMethod(startRow, startCol, distance);
        } else if (methodNumber==4) {
            upMethod(startRow, startCol, distance);
        }
    }
    public static void traverse(int currentRow, int currentColumn) {
        visitedArray[visitedArrayInteger][0] = currentRow;
        visitedArray[visitedArrayInteger][1] = currentColumn;
        visitedArrayInteger++;

        pathArray[pathArrayInteger][0] = currentRow;
        pathArray[pathArrayInteger][1]  = currentColumn;
        pathArray[pathArrayInteger][2] = distance;
        pathArray[pathArrayInteger][3] = pathArrayInteger; // the position inside the pathArray which we're in at the moment
        pathArrayInteger++;

        if (methodGroup == 1) {
            if (currentColumn < finishCol) {
                rightMain = true;
                int current_distance = distance;
                leftMethod(currentRow, currentColumn, current_distance);
                rightMethod(currentRow, currentColumn, current_distance);
            } else if (currentColumn >= finishCol) {
                leftMain = true;
                int current_distance = distance;
                rightMethod(currentRow, currentColumn, current_distance);
                leftMethod(currentRow, currentColumn, current_distance);
            }
        } else if (methodGroup == 2) {
            if (currentRow <= finishRow) {
                downMain = true;
                int current_distance = distance;
                upMethod(currentRow, currentColumn, current_distance);
                downMethod(currentRow, currentColumn, current_distance);
            } else if (currentRow > finishRow) {
                upMain = true;
                int current_distance = distance;
                downMethod(currentRow, currentColumn, current_distance);
                upMethod(currentRow, currentColumn, current_distance);
            }
        } else if (methodGroup== 3) {
            shortestPathFinding(distance, pathArrayInteger);
            getCoordinatesSubstituteArray();
        }
    }

    public static void shortestPathFinding(int distance, int integer){// in this method the first ever path that find "F" will get in to the complete array, then what ever the path that find "F" will check the fina distance with the previously added array distance and if the new path distance is less, then the new path will updated as the complete array.
        if (got_a_complete_path){
            distance2=distance;
            if (distance2<distance1){
                distance1=distance;
                currentInteger=integer;
                for (int i=0; i<currentInteger; i++){
                    for (int j=0; j<pathArray[i].length;j++){
                        completePathArray[i][j]=pathArray[i][j];
                    }
                }
            }
        }else {
            got_a_complete_path=true;
            distance1 = distance;
            currentInteger = integer;
            for (int i=0; i<currentInteger; i++){
                for (int j=0; j<pathArray[i].length;j++){
                    completePathArray[i][j]=pathArray[i][j];
                }
            }
        }
    }

    public static void printOutput(){ // printing the output
        System.out.println("Start at ("+(completePathArray[0][1]+1)+", "+(completePathArray[0][0]+1)+").");
//        int count=0;
        for (int i=1; i<currentInteger; i++){
//            count++;
            if (completePathArray[i][1]<completePathArray[i-1][1]){
                System.out.println("Move left to ("+(completePathArray[i][1]+1)+", "+(completePathArray[i][0]+1)+").");
            } else if (completePathArray[i][1]>completePathArray[i-1][1]) {
                System.out.println("Move right to ("+(completePathArray[i][1]+1)+", "+(completePathArray[i][0]+1)+").");
            } else if (completePathArray[i][0]<completePathArray[i-1][0]) {
                System.out.println("Move up to ("+(completePathArray[i][1]+1)+", "+(completePathArray[i][0]+1)+").");
            } else if (completePathArray[i][0]>completePathArray[i-1][0]) {
                System.out.println("Move down to ("+(completePathArray[i][1]+1)+", "+(completePathArray[i][0]+1)+").");
            }
        }
        System.out.println("Finish");
    }
    public static void leftMethod(int row, int column, int c_distance) {
        nodeFound = false;
        distance=c_distance;
        if (column-1 >= 0) {
            // Start traversing from the given starting point towards the left by decrementing column coordinates
            for (int l = column - 1; l >= 0; l--) {
                if (mainArray[row][l].equals("F")) { //checks whether the value stored in mainArray[row][column] element is equal to the string "0" or "F"
                    found_aPath=true;
                    leftPositionNum = l; //we get a global variable as leftPositionNum in order to check if its inside visitedArray
                    distance++;
                    break; // Stop traversing if 'O' or 'F' is met
                } else if (mainArray[row][l].equals(".")) {
                    distance++;
                    leftPositionNum=l;
                } else { //if it's not F or . means, we meet with 0
                    leftPositionNum = l + 1 ;
                    break;
                }
            }
            for (int i = 0; i < visitedArray.length; i++) {
                if (visitedArray[i][0] == row && visitedArray[i][1] == leftPositionNum) { //checks if the coordinates before meeting a rock(row and leftPositionNum) is inside the visitedArray
                    nodeFound = true;
                    break;
                }
            }
            for (int i = 0; i < substituteArray.length; i++) {
                if (substituteArray[i][0] == row && substituteArray[i][1] == leftPositionNum) { //checks if the coordinates before meeting a rock(row and leftPositionNum) is inside the visitedArray
                    nodeFound = true;
                    break;
                }
            }
            for (int i = 0; i < pathArray.length; i++) {
                if (pathArray[i][0] == row && pathArray[i][1] == leftPositionNum) { //checks if the coordinates before meeting a rock(row and leftPositionNum) is inside the visitedArray
                    if (distance<pathArray[i][2]){
                        updatedPath=true;
                        break;
                    }
                }
            }
            if (found_aPath&&updatedPath){
                methodGroup=3;
                found_aPath=false;
                updatedPath=false;
                if (leftMain) { // (leftMain == true)
                    leftMain = false;
                    traverse(row, leftPositionNum);
                } else if (rightMain) { // (rightMain == true)
                    substituteArray[substituteArrayInteger][0] = row;
                    substituteArray[substituteArrayInteger][1] = leftPositionNum;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            }
            else if (found_aPath){ // (nodeFound == true && leftMain == true)
                leftMain=false;
                found_aPath=false;
                methodGroup=3;
                traverse(row, leftPositionNum);
            } else if (updatedPath) {
                updatedPath=false;
                methodGroup = 2;
                if (leftMain) { // (leftMain == true)
                    leftMain = false;
                    traverse(row, leftPositionNum);
                } else if (rightMain) { // (rightMain == true)
                    substituteArray[substituteArrayInteger][0] = row;
                    substituteArray[substituteArrayInteger][1] = leftPositionNum;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            } else if (nodeFound && leftMain){ // (nodeFound == true && leftMain == true)
                leftMain = false;
                getCoordinatesSubstituteArray();
            }
            else if (!nodeFound) { // nodeFound == false
                methodGroup = 2;
                if (leftMain) { // (leftMain == true)
                    leftMain = false;
                    traverse(row, leftPositionNum);
                } else if (rightMain) { // (rightMain == true)
                    substituteArray[substituteArrayInteger][0] = row;
                    substituteArray[substituteArrayInteger][1] = leftPositionNum;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            }
        }
        else if (leftMain){ //(leftMain == true)
            leftMain = false;
            getCoordinatesSubstituteArray();
        }
    }

    public static void rightMethod(int row, int column, int c_distance) {
        nodeFound = false;
        distance=c_distance;
        if (column+1 < width) {
            // Start traversing from the given starting point towards the right by incrementing column coordinates
            for (int r = column+1; r < width; r++) {
                if (mainArray[row][r].equals("F")) { //checks whether the value stored in mainArray[row][column] element is equal to the string "0" or "F"
                    rightPositionNum = r; //we get a global variable as rightPositionNum in order to check if its inside visitedArray
                    distance++;
                    found_aPath=true;
                    break; // Stop traversing if 'O' or 'F' is met
                } else if (mainArray[row][r].equals(".")) {
                    distance++;
                    rightPositionNum=r;
                } else { //if it's not F or . means, we meet with 0
                    rightPositionNum = r - 1;
                    break;
                }
            }
            for (int i = 0; i < visitedArray.length; i++) {
                if (visitedArray[i][0] == row && visitedArray[i][1] == rightPositionNum) { //checks if the coordinates before meeting a rock(row and leftPositionNum) is inside the visitedArray
                    nodeFound = true;
                    break;
                }
            }
            for (int i = 0; i < substituteArray.length; i++) {
                if (substituteArray[i][0] == row && substituteArray[i][1] == rightPositionNum) { //checks if the coordinates before meeting a rock(row and leftPositionNum) is inside the visitedArray
                    nodeFound = true;
                    break;
                }
            }
            for (int i = 0; i < pathArray.length; i++) {
                if (pathArray[i][0] == row && pathArray[i][1] == rightPositionNum) { //checks if the coordinates before meeting a rock(row and leftPositionNum) is inside the visitedArray
                    if (distance<pathArray[i][2]){
                        updatedPath=true;
                        break;
                    }
                }
            }
            if (found_aPath && updatedPath){
                methodGroup=3;
                found_aPath=false;
                updatedPath=false;
                if (rightMain){ // (rightMain == true)
                    rightMain = false;
                    traverse(row, rightPositionNum);
                } else if (leftMain) { // (leftMain == true)
                    substituteArray[substituteArrayInteger][0] = row;
                    substituteArray[substituteArrayInteger][1] = rightPositionNum;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            }
            else if (found_aPath){ // (nodeFound == true && rightMain == true)
                found_aPath = false;
                rightMain = false;
                methodGroup=3;
                traverse(row, rightPositionNum);
            }
            else if (nodeFound && rightMain){ // (nodeFound == true && rightMain == true)
                rightMain = false;
                getCoordinatesSubstituteArray();
            }
            else if (updatedPath) { // nodeFound == false
                methodGroup = 2;
                if (rightMain){ //(rightMain == true)
                    rightMain = false;
                    traverse(row, rightPositionNum);
                } else if (leftMain) { //(leftMain == true)
                    substituteArray[substituteArrayInteger][0] = row;
                    substituteArray[substituteArrayInteger][1] = rightPositionNum;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            }
            else if (!nodeFound) { // nodeFound == false
                methodGroup = 2;
                if (rightMain){ //(rightMain == true)
                    rightMain = false;
                    traverse(row, rightPositionNum);
                } else if (leftMain) { //(leftMain == true)
                    substituteArray[substituteArrayInteger][0] = row;
                    substituteArray[substituteArrayInteger][1] = rightPositionNum;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            }
        } else if (rightMain){ // (rightMain == true)
            rightMain = false;
            getCoordinatesSubstituteArray();
        }
    }

    public static void downMethod(int row, int column, int c_distance) {
        nodeFound = false;
        distance=c_distance;
        if (row+1 <= height-1) {
            for (int d = row+1; d <= height-1; d++) { // Start traversing from the given starting point towards down by incrementing row coordinates
                if (mainArray[d][column].equals("F")) { //checks whether the value stored in mainArray[row][column] element is equal to the string "0" or "F"
                    downPositionNum = d; //we get a global variable as downPositionNum in order to check if its inside visitedArray
                    distance++;
                    found_aPath=true;
                    break; // Stop traversing if 'O' or 'F' is met
                } else if (mainArray[d][column].equals(".")) {
                    distance++;
                    downPositionNum=d;
                } else { //if it's not F or . means, we meet with 0
                    downPositionNum = d - 1;
                    break;
                }
            }
            for (int i = 0; i < visitedArray.length; i++) {
                if (visitedArray[i][0] == downPositionNum && visitedArray[i][1] == column) { //checks if the coordinates before meeting a rock(row and downPositionNum) is inside the visitedArray
                    nodeFound = true;
                    break;
                }
            }
            for (int i = 0; i < substituteArray.length; i++) {
                if (substituteArray[i][0] == downPositionNum && substituteArray[i][1] == column) { //checks if the coordinates before meeting a rock(row and downPositionNum) is inside the visitedArray
                    nodeFound = true;
                    break;
                }
            }
            for (int i = 0; i < pathArray.length; i++) {
                if (pathArray[i][0] == downPositionNum && pathArray[i][1] == column) { //checks if the coordinates before meeting a rock(row and downPositionNum) is inside the visitedArray
                    if (distance<pathArray[i][2]){
                        updatedPath=true;
                        break;
                    }
                }
            }
            if (found_aPath&&updatedPath){
                found_aPath=false;
                updatedPath=false;
                methodGroup=3;
                if (downMain) { //downMain == true
                    downMain = false;
                    traverse(downPositionNum, column);
                } else if (upMain) { //upMain == true
                    substituteArray[substituteArrayInteger][0] = downPositionNum;
                    substituteArray[substituteArrayInteger][1] = column;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            } else if (found_aPath) {
                leftMain=false;
                found_aPath=false;
                methodGroup=3;
                traverse(downPositionNum, column);

            } else if (nodeFound && downMain){ //  (nodeFound == true && downMain == true)
                downMain = false;
                getCoordinatesSubstituteArray();
            }
            else if (updatedPath) { // nodeFound == false
                methodGroup = 1;
                if (downMain) { // downMain == true
                    downMain = false;
                    traverse(downPositionNum, column);
                } else if (upMain) { // upMain == true
                    substituteArray[substituteArrayInteger][0] = downPositionNum;
                    substituteArray[substituteArrayInteger][1] = column;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            }
            else if (!nodeFound) { // nodeFound == false
                methodGroup = 1;
                if (downMain) { // downMain == true
                    downMain = false;
                    traverse(downPositionNum, column);
                } else if (upMain) { // upMain == true
                    substituteArray[substituteArrayInteger][0] = downPositionNum;
                    substituteArray[substituteArrayInteger][1] = column;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            }
        } else if (downMain){ // downMain == true
            downMain = false;
            getCoordinatesSubstituteArray();
        }
    }

    public static void upMethod(int row, int column, int c_distance) {
        nodeFound = false;
        distance=c_distance;
        if ( row-1 >= 0  ) {
            for (int u = row-1; u >= 0 ; u--) { // Start traversing from the given starting point towards down by incrementing row coordinates
                if (mainArray[u][column].equals("F")) { //checks whether the value stored in mainArray[row][column] element is equal to the string "0" or "F"
                    upPositionNum = u; //we get a global variable as upPositionNum in order to check if its inside visitedArray
                    distance++;
                    found_aPath=true;
                    break; // Stop traversing if 'O' or 'F' is met
                } else if (mainArray[u][column].equals(".")) {
                    distance++;
                    upPositionNum=u;
                } else { //if it's not F or . means, we meet with 0
                    upPositionNum = u + 1;
                    break;
                }
            }
            for (int i = 0; i < visitedArray.length; i++) {
                if (visitedArray[i][0] == upPositionNum && visitedArray[i][1] == column) { //checks if the coordinates before meeting a rock(row and downPositionNum) is inside the visitedArray
                    nodeFound = true;
                    break;
                }
            }
            for (int i = 0; i < substituteArray.length; i++) {
                if (substituteArray[i][0] == upPositionNum && substituteArray[i][1] == column) { //checks if the coordinates before meeting a rock(row and downPositionNum) is inside the visitedArray
                    nodeFound = true;
                    break;
                }
            }
            for (int i = 0; i < pathArray.length; i++) {
                if (pathArray[i][0] == upPositionNum && pathArray[i][1] == column) { //checks if the coordinates before meeting a rock(row and downPositionNum) is inside the visitedArray
                    if (distance<pathArray[i][2]){
                        updatedPath=true;
                        break;
                    }
                }
            }
            if (found_aPath&updatedPath){
                found_aPath=false;
                updatedPath=false;
                methodGroup=3;
                if (upMain) { // upMain == true
                    upMain = false;
                    traverse(upPositionNum, column);
                } else if (downMain) { // downMain == true
                    substituteArray[substituteArrayInteger][0] = upPositionNum;
                    substituteArray[substituteArrayInteger][1] = column;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            }
            else if (found_aPath){ // nodeFound == true && upMain == true
                upMain=false;
                found_aPath=false;
                methodGroup=3;
                traverse(upPositionNum, column);
            }
            else if (nodeFound && upMain){ // nodeFound == true && upMain == true
                upMain=false;
                getCoordinatesSubstituteArray();
            }
            else if (updatedPath) { // nodeFound == false
                methodGroup = 1;
                if (upMain) { // upMain == true
                    upMain = false;
                    traverse(upPositionNum, column);
                } else if (downMain) { //downMain == true
                    substituteArray[substituteArrayInteger][0] = upPositionNum;
                    substituteArray[substituteArrayInteger][1] = column;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            }
            else if (!nodeFound) { // nodeFound == false
                methodGroup = 1;
                if (upMain) { // upMain == true
                    upMain = false;
                    traverse(upPositionNum, column);
                } else if (downMain) { // downMain == true
                    substituteArray[substituteArrayInteger][0] = upPositionNum;
                    substituteArray[substituteArrayInteger][1] = column;
                    substituteArray[substituteArrayInteger][2] = distance;
                    substituteArray[substituteArrayInteger][3] = pathArrayInteger;
                    substituteArray[substituteArrayInteger][4] = methodGroup;
                    substituteArrayInteger++;
                }
            }
        }
        else if (upMain){ // upMain == true
            upMain=false;
            getCoordinatesSubstituteArray();
        }
    }

    public static void getCoordinatesSubstituteArray(){
        if(substituteArrayInteger > 0) {
            int row = substituteArray[substituteArrayInteger-1][0];  //get the row of last index of the substituteArray which has a value in it
            int column = substituteArray[substituteArrayInteger-1][1]; //get the column of last index of the substituteArray which has a value in it
            distance = substituteArray[substituteArrayInteger-1][2]; //get the distance travelled until the last index of the substituteArray which has a value in it
            pathArrayInteger = substituteArray[substituteArrayInteger-1][3]; //get the pathArrayInteger until the last index of the substituteArray which has a value in it
            methodGroup = substituteArray[substituteArrayInteger-1][4];
            substituteArrayInteger--;
            traverse(row, column);
        } else {
            printOutput();
        }
    }
}