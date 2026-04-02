<script lang="ts">
  import type { LineGroup as LineGroupType } from '$lib/services/lineStatusApi';
  import LineCard from './LineCard.svelte';

  let { group }: { group: LineGroupType } = $props();

  const groupLabels: Record<string, string> = {
    'Metrô': 'Metrô de São Paulo',
    'Trens': 'Companhia Paulista de Trens Metropolitanos',
  };

  let sectionLabel = $derived(groupLabels[group.name] ?? group.name);
</script>

<section data-testid="line-group" data-group-name={group.name}>
  <div class="mb-6">
    <h3 class="text-[11px] font-black text-slate-400 uppercase tracking-[0.2em]">
      {sectionLabel}
    </h3>
  </div>
  <div class="space-y-3">
    {#each group.lines as line (line.uid)}
      <LineCard {line} />
    {/each}
  </div>
</section>
