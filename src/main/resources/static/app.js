const $ = id => document.getElementById(id);
const sample = `MEERUT INSTITUTE OF TECHNOLOGY
NOTICE: Student Project Demonstration
All students of B.Tech CSE 3rd year are informed that the mini project demonstration will be held on 28 November 2026 at 10:00 AM in the respective department laboratories. Students must bring their project report, presentation slides and a working laptop. Each team will get 10 minutes for demonstration followed by questions. Students must report 15 minutes before their allotted slot. For queries, contact the project coordinator.`;
$('sampleBtn').onclick = () => { $('noticeText').value = sample; updateCount(); };
$('noticeText').addEventListener('input', updateCount);
function updateCount(){ $('count').textContent = $('noticeText').value.length + ' characters'; }

$('analyzeBtn').onclick = async () => {
  const noticeText = $('noticeText').value.trim();
  if (!noticeText) { $('message').textContent = 'Please paste a notice first.'; return; }
  $('analyzeBtn').disabled = true; $('message').textContent = 'Analyzing notice…';
  try {
    const response = await fetch('/api/notices/analyze', {
      method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify({noticeText})
    });
    const data = await response.json();
    if (!response.ok) throw new Error(data.message || 'Could not analyze notice.');
    showResult(data); $('message').textContent = 'Analysis saved to your notice history.'; loadHistory();
  } catch (error) { $('message').textContent = error.message; }
  finally { $('analyzeBtn').disabled = false; }
};
function showResult(d) {
  $('title').textContent = d.title || 'Notice analysis';
  $('summary').textContent = d.simpleSummary || '';
  $('deadlines').textContent = d.deadlines || '';
  $('eligibility').textContent = d.eligibility || '';
  $('documents').textContent = d.requiredDocuments || '';
  $('actions').textContent = d.actionChecklist || '';
  $('original').textContent = d.originalText || '';
  $('result').classList.remove('hidden');
}
function escapeHtml(value) {
  return String(value ?? '').replace(/[&<>"']/g, ch => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[ch]));
}
async function loadHistory() {
  try {
    const response = await fetch('/api/notices'); const items = await response.json();
    const markup = items.length ? items.map(item =>
      `<div class="history-item" data-id="${item.id}"><b>${escapeHtml(item.title)}</b><small>${new Date(item.createdAt).toLocaleString()}</small></div>`
    ).join('') : 'No saved notices yet.';
    $('recentList').innerHTML = markup; $('allHistory').innerHTML = markup;
    document.querySelectorAll('.history-item').forEach(el => el.addEventListener('click', () => openNotice(el.dataset.id)));
  } catch { $('recentList').textContent = 'History is unavailable until the server starts.'; }
}
async function openNotice(id) {
  const response = await fetch('/api/notices/' + id);
  if (!response.ok) return;
  const data = await response.json(); showResult(data);
  $('workspace').classList.remove('hidden'); $('historyPage').classList.add('hidden');
}
$('refreshBtn').onclick = loadHistory;
$('newBtn').onclick = () => { $('workspace').classList.remove('hidden'); $('historyPage').classList.add('hidden'); };
$('historyBtn').onclick = () => { $('workspace').classList.add('hidden'); $('historyPage').classList.remove('hidden'); loadHistory(); };
loadHistory();
