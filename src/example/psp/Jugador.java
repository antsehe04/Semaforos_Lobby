package example.psp;

public class Jugador implements Runnable {
    private int id;
    private Lobby lobby;

    public Jugador(int id, Lobby lobby){
        this.id = id;
        this.lobby = lobby;
    }

    @Override
    public void run() {
        String nombre = "Jugador " + id;

        try {
            lobby.entrarLobby(nombre);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
