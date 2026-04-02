<script lang="ts">
  import type { Line } from '$lib/services/lineStatusApi';

  let { line }: { line: Line } = $props();

  const statusColorMap: Record<string, string> = {
    OperacaoNormal: '#22c55e',
    VelocidadeReduzida: '#f59e0b',
    OperacaoParcial: '#f97316',
    Paralisada: '#ef4444',
    OperacaoEncerrada: '#6b7280',
  };

  const statusTextColorMap: Record<string, string> = {
    OperacaoNormal: 'text-green-600',
    VelocidadeReduzida: 'text-amber-600',
    OperacaoParcial: 'text-orange-600',
    Paralisada: 'text-red-600',
    OperacaoEncerrada: 'text-gray-600',
  };

  let dotColor = $derived(statusColorMap[line.status.code] ?? '#6b7280');
  let textColorClass = $derived(statusTextColorMap[line.status.code] ?? 'text-gray-600');
  let needsDarkText = $derived(
    ['#FCC540', '#FFD400', '#AFA690', '#34AEA4', '#8F8F8C'].includes(line.colorHex.toUpperCase())
  );
</script>

<div
  class="bg-white rounded-2xl border border-slate-200 p-6 flex flex-col shadow-sm transition-all hover:shadow-md"
  style="border-left: 12px solid {line.colorHex};"
  data-testid="line-card"
  data-line-number={line.number}
>
  <div class="flex justify-between items-center">
    <div class="flex items-center gap-6">
      <div
        class="w-10 h-10 rounded-xl flex items-center justify-center font-black text-lg shadow-sm"
        style="background-color: {line.colorHex}; color: {needsDarkText ? '#000' : '#fff'};"
      >
        {line.number}
      </div>
      <div>
        <div class="flex items-center gap-2 mb-0.5">
          <h4 class="text-lg font-extrabold text-slate-900">
            Linha {line.number} - {line.name}
          </h4>
          <span class="text-[9px] font-black text-slate-300 uppercase tracking-tighter">
            {line.operator}
          </span>
        </div>
      </div>
    </div>
    <div class="flex items-center gap-2 text-sm font-bold {textColorClass}">
      <div class="w-2 h-2 rounded-full" style="background-color: {dotColor};"></div>
      {line.status.label}
    </div>
  </div>

  {#if line.status.code !== 'OperacaoNormal' && line.status.description}
    <div class="mt-3 pt-3 border-t border-slate-100">
      <p class="text-sm text-slate-600" data-testid="disruption-description">
        {line.status.description}
      </p>
    </div>
  {/if}
</div>
