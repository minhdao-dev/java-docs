function buildPrevNext() {
  document.querySelectorAll('.pn-nav').forEach(function (el) { el.remove(); });

  var prevFooter = document.querySelector('.md-footer__link--prev');
  var nextFooter = document.querySelector('.md-footer__link--next');

  if (!prevFooter && !nextFooter) return;

  var article = document.querySelector('article.md-content__inner');
  if (!article) return;

  var lang = document.documentElement.lang || 'vi';
  var isEn = lang.startsWith('en');

  function makeCard(footerLink, type) {
    if (!footerLink) return '<div class="pn-spacer"></div>';

    var href = footerLink.getAttribute('href');
    var labelText = type === 'prev'
      ? (isEn ? 'Previous' : 'Trước')
      : (isEn ? 'Next' : 'Tiếp theo');

    var arrowPrev = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="pn-arrow"><path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"/></svg>';
    var arrowNext = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="pn-arrow"><path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"/></svg>';

    var inner = type === 'prev'
      ? arrowPrev + labelText
      : labelText + arrowNext;

    return '<a class="pn-card pn-card--' + type + '" href="' + href + '">' + inner + '</a>';
  }

  var nav = document.createElement('nav');
  nav.className = 'pn-nav';
  nav.setAttribute('aria-label', isEn ? 'Page navigation' : 'Điều hướng bài viết');
  nav.innerHTML = makeCard(prevFooter, 'prev') + makeCard(nextFooter, 'next');

  article.appendChild(nav);
}

if (typeof document$ !== 'undefined') {
  document$.subscribe(buildPrevNext);
} else {
  document.addEventListener('DOMContentLoaded', buildPrevNext);
}
