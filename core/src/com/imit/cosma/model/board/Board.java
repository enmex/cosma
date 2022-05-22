package com.imit.cosma.model.board;

import static com.imit.cosma.config.Config.getInstance;

import com.imit.cosma.ai.AI;
import com.imit.cosma.model.board.state.BoardState;
import com.imit.cosma.model.board.state.IdleBoardState;
import com.imit.cosma.model.board.state.ShipAttackingBoardState;
import com.imit.cosma.model.board.state.ShipMovingBoardState;
import com.imit.cosma.model.rules.Attack;
import com.imit.cosma.model.rules.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.SpaceshipBuilder;
import com.imit.cosma.util.Cloneable;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.spaceship.ShipRandomizer;

import java.util.HashSet;
import java.util.Set;

public class Board implements Cloneable {
    private Set<Point> emptySet;

    private Cell[][] cells;
    private Cell selected;
    private Cell interacted;
    private Set<Point> interactedCells;

    private Side turn;
    private int sideTurns;

    private int selectedX, selectedY;

    private int playerAdvantagePoints, enemyAdvantagePoints;

    private AI enemy;
    private Path currentPath;

    private Set<Point> availableForMove, availableForAttack;

    public Board() {
        cells = new Cell[getInstance().BOARD_SIZE][getInstance().BOARD_SIZE];
        emptySet = new HashSet<>();
        selected = new Cell(new Space());
        interacted = new Cell(new Space());
        playerAdvantagePoints = 0;
        enemyAdvantagePoints = 0;

        SpaceshipBuilder spaceshipBuilder = new SpaceshipBuilder();

        //initialise player ships
        for (int y = 0; y < getInstance().SPACESHIP_ROWS; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                Spaceship spaceship = spaceshipBuilder.setSide(Side.PLAYER)
                        .addSkeleton()
                        .addWeapon(ShipRandomizer.getRandomAmount())
                        .addMoves().build();
                cells[y][x] = new Cell(spaceship);


            }
        }

        //initialise space cells
        for (int y = getInstance().SPACESHIP_ROWS; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                cells[y][x] = new Cell();
            }
        }

        //initialise enemy ships
        for (int y = getInstance().BOARD_SIZE - getInstance().SPACESHIP_ROWS; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                Spaceship spaceship = spaceshipBuilder.setSide(Side.ENEMY)
                        .addSkeleton()
                        .addWeapon(ShipRandomizer.getRandomAmount())
                        .addMoves().build();
                cells[y][x] = new Cell(spaceship);
            }
        }

        turn = Side.PLAYER;
        interactedCells = new HashSet<>();

        availableForMove = new HashSet<>();
        availableForAttack = new HashSet<>();

        sideTurns = 0;
    }

    public void initAI(){
        enemy = new AI(clone());
    }

    public BoardState getCurrentState(int selectedX, int selectedY){
        if(inBoard(selectedX, selectedY)){
            return turn == Side.PLAYER ? calculateCurrentPlayerState(selectedX, selectedY) : calculateCurrentEnemyState();
        }
        return new IdleBoardState();
    }

    public BoardState calculateCurrentPlayerState(int selectedX, int selectedY){
        currentPath = new Path(this.selectedX, this.selectedY, selectedX, selectedY);
        if(selected.isShip() && selected.getStepMode() != StepMode.COMPLETED && selected.getSide() == turn) {
            if (selectedCanMoveTo(selectedX, selectedY)) {
                setSelectedPosition(selectedX, selectedY);
                sideTurns++;

                return new ShipMovingBoardState(turn == Side.PLAYER ? getCell(selectedX, selectedY) : selected);
            } else if (selectedCanFireTo(selectedX, selectedY)) {
                damageShip(selectedX, selectedY, selected.getDamageAmount());
                sideTurns++;

                return new ShipAttackingBoardState(selected, interacted);
            }
        }
        return new IdleBoardState();
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public BoardState calculateCurrentEnemyState(){
        enemy.update(clone());

        currentPath = enemy.getPath();
        Point source = currentPath.getSource();
        Point target = currentPath.getTarget();

        selected = cells[source.y][source.x];
        selectedX = source.x;
        selectedY = source.y;

        if (isShip(target.x, target.y)) {
            selected.setStepMode(StepMode.ATTACK);
        }

        if (selected.isShip() && selected.getStepMode() != StepMode.COMPLETED && selected.getSide() == turn) {
            if (selectedCanMoveTo(target.x, target.y)) {
                setSelectedPosition(target.x, target.y);
                sideTurns++;

                selected = cells[target.y][target.x];
                selectedX = target.x;
                selectedY = target.y;

                return new ShipMovingBoardState(selected);
            } else if (selectedCanFireTo(target.x, target.y)) {
                damageShip(target.x, target.y, selected.getDamageAmount());
                sideTurns++;

                return new ShipAttackingBoardState(selected, interacted);
            }
        }
        return new IdleBoardState();
    }

    public void updateSide(){
        if(sideTurns == 2){
            sideTurns = 0;

            turn = turn.nextTurn();

            for(Point point : interactedCells){
                cells[point.y][point.x].setStepMode(StepMode.MOVE);
            }
            interactedCells.clear();
        }
    }

    public boolean isShip(int x, int y) {
        return cells[y][x].isShip();
    }

    public boolean isShipSelected() {
        return selected.isShip();
    }

    public boolean isEnemyShip(int x, int y){
        return isShip(x, y) && selected.getSide() != cells[y][x].getSide();
    }

    public void damageShip(int shipX, int shipY, int damage){
        interacted.setContent(cells[shipY][shipX].getContent().clone());

        cells[shipY][shipX].setDamage(damage);
        selected.setStepMode(StepMode.COMPLETED);
        interactedCells.add(new Point(selectedX, selectedY));

        if(turn == Side.PLAYER){
            playerAdvantagePoints += Math.min(damage, cells[shipY][shipX].getHealthPoints());
        }
        else{
            enemyAdvantagePoints += Math.min(damage, cells[shipY][shipX].getHealthPoints());
        }

        if(cells[shipY][shipX].getContent().getHealthPoints() <= 0){
            destroyShip(shipX, shipY);
        }
    }

    private void destroyShip(int shipX, int shipY){
        cells[shipY][shipX].setContent(new Space());
    }

    public boolean isPassable(int x, int y) {
        return inBoard(x, y) && cells[y][x].isPassable();
    }

    public Cell getSelected() {
        return selected;
    }

    public boolean selectedCanMoveTo(int x, int y){
        return selected.isShip() && cells[y][x] != selected && selected.canMoveTo(selectedX, selectedY, x, y)
                && selected.getStepMode() == StepMode.MOVE && isPassable(x, y);
    }

    public boolean selectedCanFireTo(int x, int y){
        return selected != null && isShipSelected() && isShip(x, y)
                && selected.getSide() != cells[y][x].getSide() && selected.getStepMode() == StepMode.ATTACK;
    }

    public Cell getCell(int x, int y){
        return cells[y][x];
    }

    public Set<Point> getAvailableCellsForMove() {
        return selected.isShip() && selected.getStepMode() == StepMode.MOVE && selected.getSide() == turn ? availableForMove : emptySet;
    }
    public Set<Point> getAvailableCellsForMove(int x, int y){
        return isShip(x, y) && cells[y][x].getStepMode() == StepMode.MOVE ? cells[y][x].getMoves().getAvailable(this, x, y) : emptySet;
    }

    public Set<Point> getAvailableCellsForFire(){
        return selected.isShip() && selected.getStepMode() == StepMode.ATTACK ? availableForAttack : emptySet;
    }

    public Set<Point> getAvailableCellsForFire(int x, int y){
        return isShip(x, y) && cells[y][x].getStepMode() == StepMode.ATTACK ? Attack.getAvailable(this, x, y) : emptySet;
    }

    private void setSelectedPosition(int toX, int toY) {
        interacted.setContent(cells[toY][toX].getContent().clone());

        selected.swapContents(cells[toY][toX]);
        cells[toY][toX].setStepMode(StepMode.ATTACK);
        interactedCells.add(new Point(toX, toY));
    }

    public Point getAtlasCoords(int x, int y){
        return cells[y][x].getAtlasCoord();
    }

    public boolean inBoard(int x, int y){
        return x >=0 && x < 8 && y >= 0 && y < 8;
    }

    public int getSelectedX(){
        return selectedX;
    }
    public int getSelectedY(){
        return selectedY;
    }

    public Side getTurn(){
        return turn;
    }

    public Side getSide(int x, int y){
        return cells[y][x].getSide();
    }

    public float getDefaultRotation(int x, int y){
        return cells[y][x].getSide().getDefaultRotation();
    }

    public void setSelected(int x, int y){
        if(inBoard(x, y) && turn == Side.PLAYER){
            selected = cells[y][x];
            selectedX = x;
            selectedY = y;

            if(selected.isShip()) {
                availableForAttack = Attack.getAvailable(this);
                availableForMove = selected.getMoves().getAvailable(this, selectedX, selectedY);
            }
        }
    }

    public int getDamagePoints(Point target){
        return getDamagePoints(target.x, target.y);
    }

    public int getDamagePoints(int x, int y){
        return cells[y][x].getDamageAmount();
    }

    public int getMaxHealthPoints(int x, int y){
        return cells[y][x].getMaxHealthPoints();
    }

    public int getMaxHealthPoints(Point target){
        return getMaxHealthPoints(target.x, target.y);
    }

    public int getHealthPoints(Point target){
        return getHealthPoints(target.x, target.y);
    }

    public int getHealthPoints(int x, int y){
        return cells[y][x].getHealthPoints();
    }

    public void makeArtificialTurn(Path path){
        Point source = path.getSource();
        Point target = path.getTarget();

        selected = cells[source.y][source.x];
        selectedX = source.x;
        selectedY = source.y;

        if(selectedCanMoveTo(target.x, target.y)){
            setSelectedPosition(target.x, target.y);

            selected = cells[target.y][target.x];
            selectedX = target.x;
            selectedY = target.y;
        }
        else if(selectedCanFireTo(target.x, target.y)){
            damageShip(target.x, target.y, selected.getDamageAmount());
        }
    }

    public void set(Board board){
        for (int y = 0; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                this.cells[y][x].setContent(board.cells[y][x].getContent().clone());
            }
        }
        this.selected = board.selected;
        this.interactedCells = board.interactedCells;
        this.turn = board.turn;
        this.sideTurns = board.sideTurns;
        this.selectedX = board.selectedX;
        this.selectedY = board.selectedY;
        this.enemy = board.enemy;
        this.currentPath = board.currentPath;
    }

    @Override
    public Board clone(){
        Board board = new Board();
        board.interactedCells = interactedCells;
        board.enemy = enemy;
        board.selected = selected;
        board.cells = new Cell[getInstance().BOARD_SIZE][getInstance().BOARD_SIZE];
        for (int y = 0; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                board.cells[y][x] = new Cell(cells[y][x].getContent().clone());
            }
        }
        board.selectedX = selectedX;
        board.selectedY = selectedY;
        board.sideTurns = sideTurns;
        board.turn = turn;
        board.currentPath = currentPath;
        board.emptySet = emptySet;
        return board;
    }

    public int getPlayerAdvantagePoints(){
        return playerAdvantagePoints;
    }

    public int getEnemyAdvantagePoints(){
        return enemyAdvantagePoints;
    }

    public StepMode getStepMode(int x, int y) {
        return cells[y][x].getStepMode();
    }
}

