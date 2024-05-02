export const AppConstants = {
  //Auth
  LOGIN_API_URL: '/loginUser',
  TAILOR_SIGNUP_URL: '/tailor/create',
  CUSTOMER_SIGNUP_URL: '/customer/create',
  //CUSTOMER
  FIND_CUSTOMER_BY_ID: '/customer/find/',
  UPDATE_CUSTOMER: '/customer/update',

  //Tailor Service
  FIND_ALL_TAILORS: '/tailor/findAll',
  FIND_TAILOR_BY_ID: '/tailor/find/',
  UPDATE_TAILOR: '/tailor/update',

  //Appointment Service
  CREATE_APPOINTMENT: '/appointment/create',
  UPDATE_APPOINTMENT: '/appointment/update',
  FIND_APPOINTMENT_BY_ID: '/appointment/find/',
  FIND_APPOINTMENT_BY_CUSTOMER_ID: '/appointment/findAll/customer/',
  FIND_APPOINTMENT_BY_SHOP_ID: '/appointment/findAll/tailor/',
  //Category Service

  CREATE_CATEGORY: '/category/create',
  UPDATE_CATEGORY: '/category/update',
  FIND_CATEGORY_BY_ID: '/category/find/',
  FIND_CATEGORIES_BY_SHOP_ID: '/category/findAll?shopId=',
  DELETE_CATEGORY_BY_ID: '/category/delete/',
  //Measurement
  CREATE_MEASUREMENT: '/measurement/create',
  FIND_ALL_MEASUREMENTS: '/measurement/findAll/tailor/',

  //Payment
  CREATE_PAYMENT: '/payment/create',
  FIND_ALL_PAYEMNT: '/payment/findAll',
};
