<script lang="ts">
  let {
    type = 'warning',
    onRetry = () => {}
  }: {
    type?: 'warning' | 'error';
    onRetry?: () => void;
  } = $props();
</script>

{#if type === 'warning'}
  <div
    class="bg-amber-50 border border-amber-200 rounded-2xl p-4 flex items-center gap-3"
    data-testid="stale-warning"
  >
    <span class="material-symbols-outlined text-amber-500">warning</span>
    <p class="text-sm text-amber-700 font-medium flex-1">
      Os dados exibidos podem estar desatualizados. Não foi possível atualizar as informações.
    </p>
    <button
      class="px-4 py-2 bg-amber-100 hover:bg-amber-200 text-amber-800 rounded-xl text-xs font-bold uppercase tracking-wider transition-colors"
      onclick={onRetry}
      data-testid="retry-button"
    >
      Tentar novamente
    </button>
  </div>
{:else}
  <div
    class="bg-red-50 border border-red-200 rounded-3xl p-10 flex flex-col items-center justify-center text-center min-h-[400px]"
    data-testid="error-state"
  >
    <span class="material-symbols-outlined text-red-400 text-6xl mb-4">error</span>
    <h3 class="text-xl font-extrabold text-slate-900 mb-2">
      Não foi possível carregar os dados
    </h3>
    <p class="text-sm text-slate-500 mb-8 max-w-md">
      A fonte de dados está temporariamente indisponível. Tente novamente em instantes.
    </p>
    <button
      class="px-6 py-3 bg-red-100 hover:bg-red-200 text-red-800 rounded-xl text-xs font-bold uppercase tracking-wider transition-colors"
      onclick={onRetry}
      data-testid="retry-button"
    >
      Tentar novamente
    </button>
  </div>
{/if}
