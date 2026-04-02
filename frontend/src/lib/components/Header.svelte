<script lang="ts">
  let {
    lastUpdated = '',
    loading = false,
    onRefresh = () => {}
  }: {
    lastUpdated?: string;
    loading?: boolean;
    onRefresh?: () => void;
  } = $props();

  function formatDateTime(isoDate: string): string {
    if (!isoDate) return '--';
    try {
      const date = new Date(isoDate);
      return date.toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
      }) + ' ' + date.toLocaleTimeString('pt-BR', {
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch {
      return '--';
    }
  }
</script>

<header class="bg-white flex justify-between items-center w-full px-10 py-8">
  <div>
    <h2 class="text-2xl font-extrabold text-slate-900 tracking-tight">Status dos Trilhos SP</h2>
    <p class="text-sm font-medium text-slate-500">Situação operacional em tempo real</p>
  </div>
  <div class="flex items-center gap-6">
    <div class="flex items-center gap-4 text-slate-400 font-bold text-[11px] uppercase tracking-wider">
      <div class="flex items-center gap-1.5 bg-slate-50 px-3 py-1.5 rounded-full border border-slate-100">
        <span class="material-symbols-outlined text-[14px] text-slate-500">schedule</span>
        <span data-testid="last-updated">Atualizado: {formatDateTime(lastUpdated)}</span>
      </div>
      <button
        class="w-8 h-8 flex items-center justify-center rounded-full hover:bg-slate-100 transition-colors"
        onclick={onRefresh}
        disabled={loading}
        data-testid="refresh-button"
        aria-label="Atualizar dados"
      >
        <span class="material-symbols-outlined text-[18px]" class:animate-spin={loading}>sync</span>
      </button>
    </div>
  </div>
</header>
