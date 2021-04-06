package hotlinecesena.model.score;

public final class KillCountPartial extends AbstractPartial {

    private static final String NAME = "Kills";
    private static final int POINTS = 100;
    private final int killCount = 0;

    protected KillCountPartial() {
        super(NAME, POINTS);
    }

    @Override
    protected int formula() {
        return killCount;
    }

    //  @Subscribe
    //  private void handleDeathEvent(DeathEvent e) {
    //      if (world.getEnemies().contains(e.getSource()) {
    //          this.killCount++;
    //      }
    //  }
}
