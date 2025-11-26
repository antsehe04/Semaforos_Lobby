package example.psp;

public class MaquinaEmparejamiento implements Runnable {
    private final Lobby lobby;

    public MaquinaEmparejamiento(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void run() {
        System.out.println("Máquina de Emparejamiento iniciada. Esperando jugadores...");
        try {
            while (true) {
                lobby.procesarPartida();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Máquina de Emparejamiento interrumpida.");
        }
    }
}
