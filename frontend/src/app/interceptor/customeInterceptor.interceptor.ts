import { HttpInterceptorFn } from '@angular/common/http';

export const customeInterceptor: HttpInterceptorFn = (req, next) => {
  if (
    req.url.includes('/loginUser') ||
    req.url.includes('/customer/create') ||
    req.url.includes('/tailor/create')
  ) {
    next(req);
  }
  const tokenHeader = req.clone({
    setHeaders: {
      Authorization: window.sessionStorage.getItem('token') || 'undefined',
    },
  });
  return next(tokenHeader);
};
