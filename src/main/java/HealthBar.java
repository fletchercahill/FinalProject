public class HealthBar
{
    // Amount of health each player has to start
    private int maxHealth;

    public HealthBar(int maxHealth)
    {
        this.maxHealth = maxHealth;
    }

    // Returns the length of the healthbar based on the current health of the player
    public int getWidth(int currentHealth, int maxWidth)
    {
        double percent = (double) currentHealth / maxHealth;

        return (int)(percent * maxWidth);
    }
}