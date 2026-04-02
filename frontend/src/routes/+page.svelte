<script lang="ts">
  import '../app.css';
  import Sidebar from '$lib/components/Sidebar.svelte';
  import Header from '$lib/components/Header.svelte';
  import SummaryBanner from '$lib/components/SummaryBanner.svelte';
  import LineGroup from '$lib/components/LineGroup.svelte';
  import ErrorBanner from '$lib/components/ErrorBanner.svelte';
  import { fetchLineStatus, type LineStatusResponse } from '$lib/services/lineStatusApi';
  import { onMount, onDestroy } from 'svelte';

  const REFRESH_INTERVAL = 60_000;

  let data: LineStatusResponse | null = $state(null);
  let loading = $state(false);
  let error = $state(false);
  let stale = $state(false);
  let intervalId: ReturnType<typeof setInterval> | null = null;

  async function loadData() {
    loading = true;
    try {
      const result = await fetchLineStatus();
      data = result;
      error = false;
      stale = false;
    } catch {
      if (data) {
        stale = true;
      } else {
        error = true;
      }
    } finally {
      loading = false;
    }
  }

  function startAutoRefresh() {
    stopAutoRefresh();
    intervalId = setInterval(loadData, REFRESH_INTERVAL);
  }

  function stopAutoRefresh() {
    if (intervalId) {
      clearInterval(intervalId);
      intervalId = null;
    }
  }

  function handleRefresh() {
    loadData();
    startAutoRefresh();
  }

  onMount(() => {
    loadData();
    startAutoRefresh();
  });

  onDestroy(() => {
    stopAutoRefresh();
  });
</script>

<div class="bg-[#F8F9FA] font-[Inter] text-[#1a1c1d] flex min-h-screen">
  <Sidebar lastUpdated={data?.lastUpdated ?? ''} />

  <main class="ml-72 flex-1 flex flex-col min-h-screen">
    <Header
      lastUpdated={data?.lastUpdated ?? ''}
      {loading}
      onRefresh={handleRefresh}
    />

    <div class="px-10 pb-10 space-y-10">
      {#if error && !data}
        <ErrorBanner type="error" onRetry={handleRefresh} />
      {:else if data}
        {#if stale}
          <ErrorBanner type="warning" onRetry={handleRefresh} />
        {/if}

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div class="lg:col-span-2">
            <SummaryBanner groups={data.groups} />
          </div>
          <section class="bg-white rounded-3xl p-8 border border-slate-200 flex flex-col items-center justify-center text-center shadow-sm">
            <div class="w-14 h-14 bg-slate-100 rounded-2xl flex items-center justify-center mb-4 text-[#003c73]">
              <span class="material-symbols-outlined text-3xl">map</span>
            </div>
            <h4 class="text-xl font-extrabold text-slate-900 mb-2">Mapa da Rede</h4>
            <p class="text-sm text-slate-500 mb-8 max-w-[200px]">Visualize a conectividade completa de São Paulo</p>
            <button class="w-full py-4 bg-slate-100 hover:bg-slate-200 text-slate-700 font-extrabold text-xs tracking-widest uppercase rounded-2xl transition-all cursor-not-allowed opacity-50" disabled>
              Ver Mapa Interativo
            </button>
          </section>
        </div>

        {#each data.groups as group (group.name)}
          <LineGroup {group} />
        {/each}
      {:else}
        <div class="flex items-center justify-center min-h-[400px]">
          <div class="flex items-center gap-3 text-slate-400">
            <span class="material-symbols-outlined animate-spin text-3xl">progress_activity</span>
            <span class="text-lg font-medium">Carregando dados...</span>
          </div>
        </div>
      {/if}
    </div>

    <footer class="bg-white w-full py-8 mt-auto border-t border-slate-100 flex justify-between items-center px-10">
      <p class="text-xs font-bold tracking-wider text-slate-400 uppercase">© 2026 Status Metrô SP - Dados Oficiais</p>
      <div class="flex gap-8">
        <span class="text-[11px] font-black tracking-widest text-slate-400 uppercase">Privacidade</span>
        <span class="text-[11px] font-black tracking-widest text-slate-400 uppercase">Termos</span>
      </div>
    </footer>
  </main>
</div>
