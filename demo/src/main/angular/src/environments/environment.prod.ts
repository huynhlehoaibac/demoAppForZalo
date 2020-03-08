export const environment = {
  production: true,
  jwt: {
    whitelistedDomains: null,
    blacklistedRoutes: [/\/auth\/.*/]
  },
  serverUrl: '',
  authUrl: '/auth',
  apiUrl: '/api',
};
