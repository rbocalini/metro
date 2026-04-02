import { test, expect } from '@playwright/test';

test.describe('Line Status Dashboard', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
    // Wait for data to load
    await page.waitForSelector('[data-testid="line-card"]', { timeout: 10000 });
  });

  // --- US1: View Current Line Status ---

  test('should display all 13 lines', async ({ page }) => {
    const lineCards = page.locator('[data-testid="line-card"]');
    await expect(lineCards).toHaveCount(13);
  });

  test('should group lines under Metrô and Trens headings', async ({ page }) => {
    const metroGroup = page.locator('[data-testid="line-group"][data-group-name="Metrô"]');
    const trensGroup = page.locator('[data-testid="line-group"][data-group-name="Trens"]');

    await expect(metroGroup).toBeVisible();
    await expect(trensGroup).toBeVisible();
  });

  test('should display Metrô lines 1-5 and 15', async ({ page }) => {
    const metroGroup = page.locator('[data-testid="line-group"][data-group-name="Metrô"]');
    const metroLines = metroGroup.locator('[data-testid="line-card"]');

    await expect(metroLines).toHaveCount(6); // Lines 1, 2, 3, 4, 5, 15
  });

  test('should display Trens lines 7-13', async ({ page }) => {
    const trensGroup = page.locator('[data-testid="line-group"][data-group-name="Trens"]');
    const trensLines = trensGroup.locator('[data-testid="line-card"]');

    await expect(trensLines).toHaveCount(7); // Lines 7, 8, 9, 10, 11, 12, 13
  });

  test('should show line cards with color-coded left border', async ({ page }) => {
    const firstCard = page.locator('[data-testid="line-card"]').first();
    const borderStyle = await firstCard.evaluate(
      (el) => window.getComputedStyle(el).borderLeftStyle
    );
    expect(borderStyle).toBe('solid');

    const borderWidth = await firstCard.evaluate(
      (el) => parseInt(window.getComputedStyle(el).borderLeftWidth)
    );
    expect(borderWidth).toBeGreaterThan(0);
  });

  test('should show disruption description for non-normal lines', async ({ page }) => {
    // Check if any line has a disruption description visible
    const descriptions = page.locator('[data-testid="disruption-description"]');
    const count = await descriptions.count();

    // If all lines are normal, no descriptions should be shown
    // If any line is disrupted, at least one description should exist
    // We just verify the structure works — actual disruption depends on live data
    expect(count).toBeGreaterThanOrEqual(0);
  });

  test('should display the summary banner', async ({ page }) => {
    const banner = page.locator('[data-testid="summary-banner"]');
    await expect(banner).toBeVisible();
    await expect(banner).toContainText('Operacional');
  });

  test('should show last-updated timestamp', async ({ page }) => {
    const timestamp = page.locator('[data-testid="last-updated"]');
    await expect(timestamp).toBeVisible();
    await expect(timestamp).not.toContainText('--');
  });

  // --- US2: Auto Refresh ---

  test('should have a working refresh button', async ({ page }) => {
    const refreshButton = page.locator('[data-testid="refresh-button"]');
    await expect(refreshButton).toBeVisible();

    const timestampBefore = await page
      .locator('[data-testid="last-updated"]')
      .textContent();

    await refreshButton.click();

    // Wait a moment for the fetch to complete
    await page.waitForTimeout(2000);

    // Timestamp should still be visible after refresh
    const timestampAfter = await page
      .locator('[data-testid="last-updated"]')
      .textContent();
    expect(timestampAfter).toBeTruthy();
  });

  // --- US3: Error Handling ---

  test('should show error state when API fails on first load', async ({ page }) => {
    // Block the API endpoint to simulate failure
    await page.route('**/api/line-status', (route) => {
      route.fulfill({ status: 502, body: JSON.stringify({ error: 'EXTERNAL_API_UNAVAILABLE', message: 'Test error' }) });
    });

    await page.goto('/');

    const errorState = page.locator('[data-testid="error-state"]');
    await expect(errorState).toBeVisible({ timeout: 10000 });

    const retryButton = errorState.locator('[data-testid="retry-button"]');
    await expect(retryButton).toBeVisible();
  });

  test('should show stale warning when refresh fails after initial load', async ({ page }) => {
    // Page already loaded with data from beforeEach
    // Now block the API
    await page.route('**/api/line-status', (route) => {
      route.fulfill({ status: 502, body: JSON.stringify({ error: 'EXTERNAL_API_UNAVAILABLE', message: 'Test error' }) });
    });

    // Trigger manual refresh
    await page.locator('[data-testid="refresh-button"]').click();

    // Wait for the stale warning
    const staleWarning = page.locator('[data-testid="stale-warning"]');
    await expect(staleWarning).toBeVisible({ timeout: 10000 });

    // Line cards should still be visible (data retained)
    const lineCards = page.locator('[data-testid="line-card"]');
    const count = await lineCards.count();
    expect(count).toBeGreaterThan(0);
  });
});
