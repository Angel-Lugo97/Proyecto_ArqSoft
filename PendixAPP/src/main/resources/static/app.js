const parametrosUrl = new URLSearchParams(window.location.search);
const backendDesdeUrl = parametrosUrl.get('api');

if (backendDesdeUrl) {
  localStorage.setItem(
    'pendix_backend_url',
    backendDesdeUrl.replace(/\/+$/, '')
  );
}

const API_BASE_URL =
  localStorage.getItem('pendix_backend_url') || '';

function apiUrl(ruta) {
  return `${API_BASE_URL}${ruta}`;
}

const tareasBase=[
 {id:1,t:'Preparar presentación',fecha:'Hoy, 2:00 PM',cat:'Trabajo',estado:'vencido'},
 {id:2,t:'Comprar víveres',fecha:'Mañana, 9:00 AM',cat:'Personal',estado:'activo'},
 {id:3,t:'Enviar reporte semanal',fecha:'Hoy, 9:00 AM',cat:'Trabajo',estado:'completado'},
 {id:4,t:'Revisar tarea de arquitectura',fecha:'Hoy, 8:00 PM',cat:'Escuela',estado:'activo'},
 {id:5,t:'Pagar servicio',fecha:'Hoy, 8:00 AM',cat:'Casa',estado:'vencido'},
 {id:6,t:'Llamar al médico',fecha:'Ayer, 10:00 PM',cat:'Salud',estado:'vencido'},
 {id:7,t:'Leer apuntes de Java',fecha:'Viernes, 6:00 PM',cat:'Escuela',estado:'activo'},
 {id:8,t:'Ordenar documentos',fecha:'Ayer, 3:00 PM',cat:'Personal',estado:'completado'}
];
let tareas=JSON.parse(localStorage.getItem('pendixapp_tareas_java5018')||'null')||tareasBase;
let filtro='todos';
let usuario=JSON.parse(localStorage.getItem('pendixapp_usuario')||'null');
const filtros=document.getElementById('filters');
const tasks=document.getElementById('tasks');
const content=document.getElementById('content');

function guardar(){localStorage.setItem('pendixapp_tareas_java5018',JSON.stringify(tareas));}
function conteos(){return{todos:tareas.length,activo:tareas.filter(x=>x.estado==='activo').length,completado:tareas.filter(x=>x.estado==='completado').length,vencido:tareas.filter(x=>x.estado==='vencido').length};}
function renderFiltros(){const c=conteos();const data=[['todos','Todos',c.todos],['activo','Activos',c.activo],['completado','Listos',c.completado],['vencido','Vencidos',c.vencido]];filtros.innerHTML=data.map(x=>`<button class="filter ${filtro===x[0]?'active':''} ${x[0]==='vencido'?'red':''}" onclick="setFiltro('${x[0]}')">${x[1]} <span>${x[2]}</span></button>`).join('');document.getElementById('notifBadge').textContent=c.vencido;}
function renderTasks(){let lista=filtro==='todos'?tareas:tareas.filter(x=>x.estado===filtro);tasks.innerHTML=lista.map(x=>{let cls=x.estado==='completado'?'completed':x.estado==='vencido'?'vencido':'';let label=x.estado==='completado'?'<span class="pill done">COMPLETADO</span>':x.estado==='vencido'?'<span class="pill expired">VENCIDO</span>':'<span class="pill activeP">ACTIVO</span>';let action=x.estado==='completado'?`<button class="undo" onclick="deshacer(${x.id})">↩ Deshacer</button>`:`<button class="mini" onclick="completar(${x.id})">✓</button>`;return `<article class="task ${cls}"><div class="check" onclick="completar(${x.id})">${x.estado==='completado'?'✓':''}</div><div><h4>${x.t}</h4><p>📅 ${x.fecha} · 🏳 ${x.cat}</p></div><div style="text-align:right">${label}<br><br><div class="actions">${action}<button class="mini" onclick="editTask(${x.id})">✎</button><button class="mini" onclick="deleteTask(${x.id})">🗑</button></div></div></article>`}).join('')||'<p class="muted">No hay pendientes en este filtro.</p>';}
function renderNotificaciones(){const vencidas=tareas.filter(x=>x.estado==='vencido');const html=vencidas.slice(0,3).map(x=>`<div class="notif"><span class="warn">⚠</span><div><h4>${x.t} <span class="pill expired">VENCIDA</span></h4><p>Programada para ${x.fecha}</p></div><strong style="color:#ff8b8b">Atrasada ›</strong></div>`).join('')||'<p class="muted">No tienes notificaciones vencidas.</p>';document.getElementById('notificacionesHome').innerHTML=html;}
function renderCalendar(){const grupos={};tareas.forEach(t=>{let k=t.fecha.split(',')[0];if(!grupos[k])grupos[k]=[];grupos[k].push(t);});document.getElementById('calendarList').innerHTML=Object.keys(grupos).map(d=>`<div class="calendar-day"><strong>${d}</strong>${grupos[d].map(t=>`<p>${estadoIcon(t.estado)} ${t.t} — ${t.fecha}</p>`).join('')}</div>`).join('');}
function renderRecordatorios(){document.getElementById('reminderList').innerHTML=tareas.map(t=>`<div class="notif"><span class="warn">${t.estado==='vencido'?'⚠':'🔔'}</span><div><h4>${t.t} ${badge(t.estado)}</h4><p>${t.fecha} · ${t.cat}</p></div><strong>${t.estado==='vencido'?'Revisar':'Activo'} ›</strong></div>`).join('');}
function badge(e){return e==='completado'?'<span class="pill done">COMPLETADO</span>':e==='vencido'?'<span class="pill expired">VENCIDA</span>':'<span class="pill activeP">ACTIVO</span>';}
function estadoIcon(e){return e==='completado'?'✅':e==='vencido'?'⚠':'🔔';}
function setFiltro(f){filtro=f;showView('pendientes',false);render();setTimeout(()=>document.getElementById('tasksSection').scrollIntoView({behavior:'smooth',block:'start'}),70);}
function completar(id){tareas=tareas.map(x=>x.id===id?{...x,estado:'completado'}:x);guardar();render();toast('Pendiente marcado como completado');}
function deshacer(id){tareas=tareas.map(x=>x.id===id?{...x,estado:'activo'}:x);guardar();render();toast('Pendiente regresado a activo');}
function deleteTask(id){const item=tareas.find(x=>x.id===id);if(confirm('¿Eliminar "'+item.t+'"?')){tareas=tareas.filter(x=>x.id!==id);guardar();render();toast('Pendiente eliminado');}}
function openTaskForm(){document.getElementById('taskModalTitle').textContent='Nuevo pendiente';document.getElementById('taskId').value='';document.getElementById('taskTitle').value='';document.getElementById('taskDate').value='';document.getElementById('taskTime').value='';document.getElementById('taskCategory').value='Personal';document.getElementById('taskState').value='activo';document.getElementById('taskModal').classList.add('open');}
function editTask(id){const t=tareas.find(x=>x.id===id);document.getElementById('taskModalTitle').textContent='Modificar pendiente';document.getElementById('taskId').value=t.id;document.getElementById('taskTitle').value=t.t;document.getElementById('taskCategory').value=t.cat;document.getElementById('taskState').value=t.estado;document.getElementById('taskDate').value='';document.getElementById('taskTime').value='';document.getElementById('taskModal').classList.add('open');}
function closeTaskForm(){document.getElementById('taskModal').classList.remove('open');}
function saveTask(){const id=document.getElementById('taskId').value;const title=document.getElementById('taskTitle').value.trim();const cat=document.getElementById('taskCategory').value.trim()||'Personal';const estado=document.getElementById('taskState').value;const date=document.getElementById('taskDate').value;const time=document.getElementById('taskTime').value;let fecha='Sin fecha';if(date||time){fecha=(date||'Pendiente')+(time?', '+time:'');}if(!title){toast('Escribe el título del pendiente');return;}if(id){tareas=tareas.map(x=>x.id==id?{...x,t:title,cat,estado,fecha:fecha==='Sin fecha'?x.fecha:fecha}:x);toast('Pendiente modificado');}else{tareas.unshift({id:Date.now(),t:title,fecha,cat,estado});toast('Pendiente agregado');}guardar();closeTaskForm();render();setFiltro('todos');}
function showView(view,scrollTop=true){document.querySelectorAll('.view').forEach(v=>v.classList.remove('active-view'));document.getElementById('view'+cap(view)).classList.add('active-view');document.querySelectorAll('.nav').forEach(n=>n.classList.toggle('active',n.dataset.nav===view));if(view==='calendario')renderCalendar();if(view==='recordatorios')renderRecordatorios();if(scrollTop)content.scrollTo({top:0,behavior:'smooth'});}
function cap(s){return s.charAt(0).toUpperCase()+s.slice(1);}
function openLogin(crear=false){document.getElementById('loginTitle').textContent=crear?'Crear cuenta':'Iniciar sesión';document.getElementById('loginModal').classList.add('open');}
function closeLogin(){document.getElementById('loginModal').classList.remove('open');}
function login(){const name=document.getElementById('loginName').value.trim()||'Usuario';const email=document.getElementById('loginEmail').value.trim()||'usuario@pendix.app';usuario={name,email};localStorage.setItem('pendixapp_usuario',JSON.stringify(usuario));closeLogin();renderSession();toast('Sesión iniciada como '+name);}
function logout(){usuario=null;localStorage.removeItem('pendixapp_usuario');renderSession();toast('Sesión cerrada');}
function renderSession(){document.getElementById('sessionText').textContent=usuario?'Sesión: '+usuario.name:'Tus pendientes, bajo control';}
function selectPlan(p){toast('Plan seleccionado: '+p);openLogin();}
function exportData(){navigator.clipboard?.writeText(JSON.stringify(tareas,null,2));toast('Datos copiados al portapapeles');}
function resetData(){if(confirm('¿Restaurar los datos de ejemplo?')){tareas=[...tareasBase];guardar();filtro='todos';render();toast('Datos restaurados');}}
function toast(msg){const t=document.getElementById('toast');t.textContent=msg;t.classList.remove('show');void t.offsetWidth;t.classList.add('show');}
function render(){renderFiltros();renderTasks();renderNotificaciones();renderCalendar();renderRecordatorios();renderSession();}
render();

let serverVersion=null;
async function watchServerVersion(){
  if(!API_BASE_URL)return;
  try{
    const response=await fetch(apiUrl('/version'),{cache:'no-store'});
    if(!response.ok)return;
    const data=await response.json();
    if(serverVersion===null){serverVersion=data.version;return;}
    if(serverVersion!==data.version){window.location.reload();}
  }catch(_ignored){
    // Durante la recompilación el servidor puede estar fuera de línea unos instantes.
  }
}
if(API_BASE_URL){
  watchServerVersion();
  setInterval(watchServerVersion,2000);
}
