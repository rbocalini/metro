<script lang="ts">
  import type { LineGroup } from '$lib/services/lineStatusApi';

  let { groups = [] }: { groups: LineGroup[] } = $props();

  let totalLines = $derived(groups.reduce((sum, g) => sum + g.lines.length, 0));
  let normalLines = $derived(
    groups.reduce(
      (sum, g) => sum + g.lines.filter((l) => l.status.code === 'OperacaoNormal').length,
      0
    )
  );
  let operationalPct = $derived(totalLines > 0 ? Math.round((normalLines / totalLines) * 100) : 0);

  function groupStatus(group: LineGroup): string {
    const disrupted = group.lines.filter((l) => l.status.code !== 'OperacaoNormal').length;
    if (disrupted === 0) return 'Normal';
    return `${disrupted} Alerta${disrupted > 1 ? 's' : ''}`;
  }
</script>

<section
  class="relative overflow-hidden rounded-3xl bg-gradient-to-br from-[#002e5d] to-[#004f9f] text-white p-10 shadow-lg min-h-[240px] flex flex-col justify-center"
  data-testid="summary-banner"
>
  <div class="relative z-10">
    <h3 class="text-5xl font-black mb-4">{operationalPct}% Operacional</h3>
    <p class="text-blue-100 font-medium text-lg max-w-md mb-8 leading-relaxed">
      {#if operationalPct === 100}
        Toda a malha ferroviária opera sem intercorrências no momento.
      {:else}
        A maior parte da malha ferroviária opera sem intercorrências no momento.
      {/if}
    </p>
    <div class="flex gap-3">
      {#each groups as group}
        <span
          class="px-4 py-2 bg-white/10 backdrop-blur-md rounded-xl text-[11px] font-extrabold tracking-widest uppercase border border-white/10"
        >
          {group.name}: {groupStatus(group)}
        </span>
      {/each}
    </div>
  </div>
  <div class="absolute right-[-20px] bottom-[-20px] opacity-10 pointer-events-none">
    <span class="material-symbols-outlined !text-[240px]">train</span>
  </div>
</section>
