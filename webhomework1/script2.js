// 切换图片
function changeSlide() {
    const slides = document.querySelectorAll('.slide');
    const activeSlide = document.querySelector('.slide:first-child');
    const nextSlide = activeSlide.nextElementSibling || slides[0];
  
    activeSlide.classList.remove('active');
    nextSlide.classList.add('active');
  }
  
  // 定时切换图片
  setInterval(changeSlide, 5000);