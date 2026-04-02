<script lang="ts">
  let { lastUpdated = '' }: { lastUpdated?: string } = $props();

  function formatTime(isoDate: string): string {
    if (!isoDate) return '--:--';
    try {
      const date = new Date(isoDate);
      return date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    } catch {
      return '--:--';
    }
  }
</script>

<aside class="fixed left-0 top-0 h-full flex flex-col p-6 bg-white w-72 border-r border-slate-200 z-50">
  <div class="mb-10 px-2 flex items-center gap-3">
    <div class="w-10 h-10 rounded-xl flex items-center justify-center bg-[#003c73] text-white overflow-hidden shrink-0">
      <span class="material-symbols-outlined text-2xl">train</span>
    </div>
    <div>
      <h1 class="text-lg font-bold text-slate-900 leading-tight">Status Metrô SP</h1>
      <p class="text-[10px] font-bold text-slate-400 uppercase tracking-tighter">
        Atualizado {formatTime(lastUpdated)}
      </p>
    </div>
  </div>

  <nav class="flex-1 space-y-1">
    <a
      class="flex items-center gap-3 px-4 py-3 bg-slate-100 text-[#003c73] rounded-xl font-bold text-sm tracking-wide uppercase transition-all"
      href="/"
    >
      <span class="material-symbols-outlined">dashboard</span>
      <span>Dashboard</span>
    </a>
    <a
      class="flex items-center gap-3 px-4 py-3 text-slate-500 hover:bg-slate-50 rounded-xl text-sm tracking-wide uppercase font-bold transition-all cursor-not-allowed opacity-50"
      href="#sobre"
      onclick={(e: MouseEvent) => e.preventDefault()}
    >
      <span class="material-symbols-outlined">info</span>
      <span>Sobre</span>
    </a>
    <a
      class="flex items-center gap-3 px-4 py-3 text-slate-500 hover:bg-slate-50 rounded-xl text-sm tracking-wide uppercase font-bold transition-all cursor-not-allowed opacity-50"
      href="#ajuda"
      onclick={(e: MouseEvent) => e.preventDefault()}
    >
      <span class="material-symbols-outlined">help</span>
      <span>Ajuda</span>
    </a>
    <a
      class="flex items-center gap-3 px-4 py-3 text-slate-500 hover:bg-slate-50 rounded-xl text-sm tracking-wide uppercase font-bold transition-all cursor-not-allowed opacity-50"
      href="#contato"
      onclick={(e: MouseEvent) => e.preventDefault()}
    >
      <span class="material-symbols-outlined">mail</span>
      <span>Contato</span>
    </a>
  </nav>

  <div class="mt-auto p-4 bg-slate-50 rounded-2xl border border-slate-100">
    <p class="text-[10px] text-slate-400 font-bold uppercase tracking-widest mb-2">Monitoramento</p>
    <div class="flex items-center gap-2">
      <div class="w-2 h-2 rounded-full bg-green-500 animate-pulse"></div>
      <span class="text-xs font-bold text-slate-700">Sistema Ativo</span>
    </div>
  </div>
</aside>
