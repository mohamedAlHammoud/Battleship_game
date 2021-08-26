package client.domain;

//Network Commands **NOT FINISHED**
public final class NC
{
	public static final byte ERROR = 0, SET_NAME = 1, END_SESSION = 2, MATCH_STARTED = 3, SHIP_PLACEMENT = 4,
			CLIENT_TURN = 5, SET_SOCKET = 6, WRONG_TURN = 7, CLIENT_SHOT = 8, REMATCH = 9, CHAT_MESSAGE = 10,
			PLAYER_BOARD = 11, OPPONENT_BOARD = 12, CLIENT_WAIT = 13, CLIENT_WON = 14, CLIENT_LOST = 15,
			RESET_SHIPS = 16;
}