/* ═══════════════════════════════════════════════════════════════════════════
   ATLAS JOURNEY — Project Documentation Scripts
   ═══════════════════════════════════════════════════════════════════════════ */

document.addEventListener('DOMContentLoaded', function() {

  // ── Reading Progress ──────────────────────────────────────────────────
  const progressFill = document.querySelector('.progress-fill');
  const progressText = document.querySelector('.progress-text');

  if (progressFill && progressText) {
    window.addEventListener('scroll', function() {
      const scrollTop = window.scrollY;
      const docHeight = document.documentElement.scrollHeight - window.innerHeight;
      const progress = Math.min(100, Math.round((scrollTop / docHeight) * 100));
      progressFill.style.width = progress + '%';
      progressText.textContent = progress + '%';
    });
  }

  // ── Active Nav Link Highlighting ──────────────────────────────────────
  const navLinks = document.querySelectorAll('.nav-link');
  const sections = [];

  document.querySelectorAll('h2[id], h3[id]').forEach(function(el) {
    sections.push({
      id: el.id,
      top: el.offsetTop,
      height: el.offsetHeight
    });
  });

  function updateActiveNav() {
    const scrollY = window.scrollY + 120;
    let activeId = null;

    for (let i = sections.length - 1; i >= 0; i--) {
      if (scrollY >= sections[i].top) {
        activeId = sections[i].id;
        break;
      }
    }

    navLinks.forEach(function(link) {
      link.classList.remove('active');
      if (link.getAttribute('href') === '#' + activeId) {
        link.classList.add('active');
      }
    });
  }

  window.addEventListener('scroll', updateActiveNav);
  updateActiveNav();

  // ── Question Toggle ───────────────────────────────────────────────────
  document.querySelectorAll('.q-item').forEach(function(item) {
    item.addEventListener('click', function() {
      this.classList.toggle('open');
    });
  });

  // ── Smooth Scroll ─────────────────────────────────────────────────────
  document.querySelectorAll('a[href^="#"]').forEach(function(anchor) {
    anchor.addEventListener('click', function(e) {
      e.preventDefault();
      const target = document.querySelector(this.getAttribute('href'));
      if (target) {
        target.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    });
  });

  // ── Keyboard Navigation ──────────────────────────────────────────────
  document.addEventListener('keydown', function(e) {
    // Ctrl+F: Focus search (not implemented but prevented default)
    // Arrow navigation between sections
    if (e.key === 'ArrowDown' || e.key === 'ArrowUp') {
      const active = document.querySelector('.nav-link.active');
      if (active) {
        const links = Array.from(navLinks);
        const idx = links.indexOf(active);
        let next;
        if (e.key === 'ArrowDown') {
          next = links[Math.min(idx + 1, links.length - 1)];
        } else {
          next = links[Math.max(idx - 1, 0)];
        }
        if (next) {
          const target = document.querySelector(next.getAttribute('href'));
          if (target) {
            target.scrollIntoView({ behavior: 'smooth', block: 'start' });
          }
        }
      }
    }
  });

  // ── Print button (footer) ──────────────────────────────────────────────
  const footerEl = document.querySelector('.doc-footer');
  if (footerEl) {
    const printBtn = document.createElement('button');
    printBtn.textContent = 'Print / Export PDF';
    printBtn.style.cssText = `
      background: var(--green);
      color: #fff;
      border: none;
      padding: 8px 16px;
      border-radius: 6px;
      font-family: inherit;
      font-size: 12px;
      font-weight: 500;
      cursor: pointer;
      transition: all .2s ease;
    `;
    printBtn.addEventListener('mouseenter', function() {
      this.style.background = '#1b4332';
    });
    printBtn.addEventListener('mouseleave', function() {
      this.style.background = '#2d6a4f';
    });
    printBtn.addEventListener('click', function() {
      window.print();
    });
    footerEl.appendChild(printBtn);
  }

  // ── Collapsible Code Examples ──────────────────────────────────────────
  document.querySelectorAll('.algo-card').forEach(function(card) {
    const header = card.querySelector('.algo-header');
    const content = card.querySelectorAll('p, pre, ul, ol, .table-wrap');
    if (header && content.length > 0) {
      header.style.cursor = 'pointer';
      header.addEventListener('click', function() {
        content.forEach(function(el) {
          el.style.display = (el.style.display === 'none') ? '' : 'none';
        });
      });
    }
  });

  console.log('Atlas Journey Docs initialized');
  console.log('Sections: ' + sections.length);
  console.log('Questions: ' + document.querySelectorAll('.q-item').length);
});
