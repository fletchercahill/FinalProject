public class HealthBar
{

    private int maxHealth;

    public HealthBar(int maxHealth)
    {
        this.maxHealth = maxHealth;
    }

    public int getWidth(int currentHealth, int maxWidth)
    {
        double percent = (double) currentHealth / maxHealth;

        return (int)(percent * maxWidth);
    }
}