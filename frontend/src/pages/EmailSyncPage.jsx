import React, { useState, useEffect } from 'react';
import { emailSyncService } from '../services/emailSyncService';
import { useToast } from '../context/ToastContext';

export default function EmailSyncPage() {
  const { addToast } = useToast();

  const [config, setConfig] = useState({
    configured: false,
    emailAddress: '',
    imapHost: 'imap.gmail.com',
    imapPort: 993,
    appPassword: '',
    autoSyncEnabled: true,
    lastSyncedAt: null,
    lastSyncStatus: null,
    lastSyncMessage: null,
  });

  const [loadingConfig, setLoadingConfig] = useState(true);
  const [saving, setSaving] = useState(false);
  const [testing, setTesting] = useState(false);
  const [syncing, setSyncing] = useState(false);
  const [simulating, setSimulating] = useState(false);

  const [syncResult, setSyncResult] = useState(null);
  const [showPassword, setShowPassword] = useState(false);
  const [showHelp, setShowHelp] = useState(false);

  // Custom Simulator state
  const [simSender, setSimSender] = useState('jobs-listings@linkedin.com');
  const [simSubject, setSimSubject] = useState('You applied to Senior Java Developer at Microsoft');
  const [simBody, setSimBody] = useState('Thank you for applying to Microsoft through LinkedIn Easy Apply.');

  useEffect(() => {
    loadConfig();
  }, []);

  const loadConfig = async () => {
    try {
      setLoadingConfig(true);
      const data = await emailSyncService.getConfig();
      setConfig(prev => ({
        ...prev,
        ...data,
        appPassword: '', // keep blank for security unless editing
      }));
    } catch (err) {
      addToast('Failed to load email configuration', 'error');
    } finally {
      setLoadingConfig(false);
    }
  };

  const handleProviderSelect = (e) => {
    const provider = e.target.value;
    if (provider === 'gmail') {
      setConfig(prev => ({ ...prev, imapHost: 'imap.gmail.com', imapPort: 993 }));
    } else if (provider === 'outlook') {
      setConfig(prev => ({ ...prev, imapHost: 'outlook.office365.com', imapPort: 993 }));
    } else if (provider === 'yahoo') {
      setConfig(prev => ({ ...prev, imapHost: 'imap.mail.yahoo.com', imapPort: 993 }));
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    if (!config.emailAddress || !config.appPassword) {
      addToast('Please provide both email address and app password', 'error');
      return;
    }

    try {
      setSaving(true);
      const updated = await emailSyncService.saveConfig({
        emailAddress: config.emailAddress,
        imapHost: config.imapHost,
        imapPort: config.imapPort,
        appPassword: config.appPassword,
        autoSyncEnabled: config.autoSyncEnabled,
      });
      setConfig(prev => ({ ...prev, ...updated, configured: true, appPassword: '' }));
      addToast('Email configuration saved successfully!', 'success');
    } catch (err) {
      const msg = err.response?.data?.message || 'Failed to save configuration';
      addToast(msg, 'error');
    } finally {
      setSaving(false);
    }
  };

  const handleTestConnection = async () => {
    try {
      setTesting(true);
      await emailSyncService.testConnection();
      addToast('Connection successful! Connected to mailbox.', 'success');
    } catch (err) {
      const msg = err.response?.data?.message || 'Connection failed';
      addToast(msg, 'error');
    } finally {
      setTesting(false);
    }
  };

  const handleSyncNow = async () => {
    try {
      setSyncing(true);
      const result = await emailSyncService.syncNow();
      setSyncResult(result);
      if (result.status === 'SUCCESS') {
        addToast(
          `Sync completed! Created: ${result.applicationsCreated}, Updated: ${result.applicationsUpdated}`,
          'success'
        );
        loadConfig();
      } else {
        addToast(result.message || 'Sync failed', 'error');
      }
    } catch (err) {
      const msg = err.response?.data?.message || 'Sync failed';
      addToast(msg, 'error');
    } finally {
      setSyncing(false);
    }
  };

  const handleSimulate = async (sender, subject, body) => {
    try {
      setSimulating(true);
      const result = await emailSyncService.simulate({ sender, subject, body });
      setSyncResult(result);
      if (result.status === 'SUCCESS') {
        addToast(
          `Processed! ${result.applicationsCreated > 0 ? 'Created application' : ''} ${result.applicationsUpdated > 0 ? 'Updated application' : ''}`,
          'success'
        );
      } else {
        addToast(result.message || 'Simulation warning', 'warning');
      }
    } catch (err) {
      const msg = err.response?.data?.message || 'Simulation failed';
      addToast(msg, 'error');
    } finally {
      setSimulating(false);
    }
  };

  if (loadingConfig) {
    return (
      <div className="container py-8">
        <div className="skeleton h-12 w-1/3 mb-6"></div>
        <div className="skeleton h-64 w-full"></div>
      </div>
    );
  }

  return (
    <div className="container py-8 max-w-6xl">
      {/* Header */}
      <div className="mb-8">
        <div className="flex items-center gap-3 mb-2">
          <div className="w-10 h-10 rounded-xl bg-primary-600/20 text-primary-400 flex items-center justify-center font-bold text-xl border border-primary-500/30">
            ✉
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white tracking-tight">Automated Email Application Reader</h1>
            <p className="text-sm text-slate-400">
              Hands-free tracking: JobPulse automatically monitors and parses confirmation emails from LinkedIn, Naukri, Indeed, and employers.
            </p>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
        {/* Left Column: Settings Form */}
        <div className="lg:col-span-2 space-y-6">
          <div className="card p-6 border border-slate-700/60 bg-slate-900/60 shadow-xl backdrop-blur-sm">
            <div className="flex items-center justify-between mb-5">
              <h2 className="text-lg font-semibold text-white flex items-center gap-2">
                <span>Inbox Credentials</span>
                {config.configured && (
                  <span className="badge badge-offer text-xs px-2.5 py-0.5">Configured</span>
                )}
              </h2>
              <button
                type="button"
                onClick={() => setShowHelp(!showHelp)}
                className="text-xs text-primary-400 hover:text-primary-300 transition-colors underline"
              >
                {showHelp ? 'Hide Gmail Guide' : 'How to get Gmail App Password?'}
              </button>
            </div>

            {/* Help accordion */}
            {showHelp && (
              <div className="mb-6 p-4 rounded-lg bg-primary-950/40 border border-primary-800/50 text-xs text-slate-300 space-y-2">
                <p className="font-semibold text-primary-300">How to generate a Gmail App Password in 60 seconds:</p>
                <ol className="list-decimal list-inside space-y-1 text-slate-300">
                  <li>Visit your Google Account Security page: <a href="https://myaccount.google.com/apppasswords" target="_blank" rel="noreferrer" className="text-primary-400 underline">myaccount.google.com/apppasswords</a></li>
                  <li>Ensure <strong>2-Step Verification</strong> is turned ON for your account.</li>
                  <li>Under &quot;App Passwords&quot;, enter <strong>JobPulse</strong> as the app name and click <strong>Create</strong>.</li>
                  <li>Copy the 16-character generated password and paste it into the App Password field below.</li>
                </ol>
                <p className="text-slate-400 text-[11px] pt-1">
                  <em>Note: Your credentials only connect securely via direct SSL IMAP to check for application confirmation emails.</em>
                </p>
              </div>
            )}

            <form onSubmit={handleSave} className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-medium text-slate-400 mb-1">Email Provider</label>
                  <select
                    onChange={handleProviderSelect}
                    className="input w-full"
                    defaultValue="gmail"
                  >
                    <option value="gmail">Google Gmail (imap.gmail.com)</option>
                    <option value="outlook">Microsoft Outlook / Hotmail</option>
                    <option value="yahoo">Yahoo Mail</option>
                    <option value="custom">Custom IMAP Server</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-medium text-slate-400 mb-1">Email Address</label>
                  <input
                    type="email"
                    required
                    value={config.emailAddress || ''}
                    onChange={(e) => setConfig({ ...config, emailAddress: e.target.value })}
                    placeholder="your-email@gmail.com"
                    className="input w-full"
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="md:col-span-2">
                  <label className="block text-xs font-medium text-slate-400 mb-1">
                    App Password (or Mailbox Password)
                  </label>
                  <div className="relative">
                    <input
                      type={showPassword ? 'text' : 'password'}
                      required={!config.configured}
                      value={config.appPassword || ''}
                      onChange={(e) => setConfig({ ...config, appPassword: e.target.value })}
                      placeholder={config.configured ? '•••••••••••••••• (Leave blank to keep current)' : '16-character app password'}
                      className="input w-full pr-16"
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-2 top-1/2 -translate-y-1/2 text-xs text-slate-400 hover:text-white px-2 py-1"
                    >
                      {showPassword ? 'Hide' : 'Show'}
                    </button>
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-medium text-slate-400 mb-1">IMAP Port</label>
                  <input
                    type="number"
                    value={config.imapPort || 993}
                    onChange={(e) => setConfig({ ...config, imapPort: parseInt(e.target.value) || 993 })}
                    className="input w-full"
                  />
                </div>
              </div>

              <div className="flex items-center gap-3 pt-2">
                <input
                  type="checkbox"
                  id="autoSync"
                  checked={config.autoSyncEnabled}
                  onChange={(e) => setConfig({ ...config, autoSyncEnabled: e.target.checked })}
                  className="rounded border-slate-700 bg-slate-800 text-primary-500 focus:ring-primary-500 w-4 h-4 cursor-pointer"
                />
                <label htmlFor="autoSync" className="text-xs text-slate-300 cursor-pointer">
                  Enable background auto-sync (checks for new emails every 15 minutes)
                </label>
              </div>

              <div className="flex items-center justify-between pt-4 border-t border-slate-800">
                <div className="flex gap-3">
                  <button
                    type="submit"
                    disabled={saving}
                    className="btn btn-primary text-sm px-5 py-2.5"
                  >
                    {saving ? 'Saving...' : 'Save Configuration'}
                  </button>
                  {config.configured && (
                    <button
                      type="button"
                      onClick={handleTestConnection}
                      disabled={testing}
                      className="btn btn-secondary text-sm px-4 py-2.5"
                    >
                      {testing ? 'Testing...' : 'Test Connection'}
                    </button>
                  )}
                </div>
              </div>
            </form>
          </div>
        </div>

        {/* Right Column: Sync Now & Status */}
        <div className="space-y-6">
          <div className="card p-6 border border-slate-700/60 bg-slate-900/60 shadow-xl">
            <h2 className="text-base font-semibold text-white mb-4">Sync Actions</h2>
            <button
              onClick={handleSyncNow}
              disabled={syncing || !config.configured}
              className={`btn w-full py-3.5 flex items-center justify-center gap-2 font-medium text-sm ${
                config.configured
                  ? 'btn-primary shadow-lg shadow-primary-500/20'
                  : 'btn-secondary opacity-50 cursor-not-allowed'
              }`}
            >
              <span className={`text-lg ${syncing ? 'animate-spin' : ''}`}>🔄</span>
              {syncing ? 'Scanning Mailbox...' : 'Sync Mailbox Now'}
            </button>
            {!config.configured && (
              <p className="text-[11px] text-amber-400 mt-2 text-center">
                Configure your email credentials on the left first to enable live sync.
              </p>
            )}

            <div className="mt-6 pt-5 border-t border-slate-800 space-y-3">
              <div className="flex justify-between text-xs">
                <span className="text-slate-400">Last Synced:</span>
                <span className="text-slate-200 font-medium">
                  {config.lastSyncedAt
                    ? new Date(config.lastSyncedAt).toLocaleString()
                    : 'Never'}
                </span>
              </div>
              <div className="flex justify-between text-xs">
                <span className="text-slate-400">Sync Status:</span>
                <span className={`font-semibold ${config.lastSyncStatus === 'SUCCESS' ? 'text-emerald-400' : config.lastSyncStatus === 'FAILED' ? 'text-rose-400' : 'text-slate-400'}`}>
                  {config.lastSyncStatus || 'Idle'}
                </span>
              </div>
              {config.lastSyncMessage && (
                <div className="text-[11px] text-slate-400 bg-slate-800/40 p-2.5 rounded border border-slate-800">
                  {config.lastSyncMessage}
                </div>
              )}
            </div>
          </div>

          {/* Sync Result Summary */}
          {syncResult && (
            <div className="card p-5 border border-primary-700/50 bg-primary-950/20">
              <h3 className="text-sm font-semibold text-primary-300 mb-2">Sync Report</h3>
              <div className="grid grid-cols-2 gap-2 text-xs mb-3">
                <div className="p-2 bg-slate-900/60 rounded border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">Created</span>
                  <span className="text-emerald-400 font-bold text-base">{syncResult.applicationsCreated}</span>
                </div>
                <div className="p-2 bg-slate-900/60 rounded border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">Updated</span>
                  <span className="text-cyan-400 font-bold text-base">{syncResult.applicationsUpdated}</span>
                </div>
              </div>
              {syncResult.details && syncResult.details.length > 0 && (
                <ul className="text-xs text-slate-300 space-y-1 max-h-40 overflow-y-auto pr-1">
                  {syncResult.details.map((d, idx) => (
                    <li key={idx} className="flex items-start gap-1.5 text-[11px]">
                      <span className="text-emerald-400">•</span>
                      <span>{d}</span>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          )}
        </div>
      </div>

      {/* Simulator Section */}
      <div className="card p-6 border border-slate-700/60 bg-slate-900/60 shadow-xl mb-8">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
          <div>
            <h2 className="text-lg font-semibold text-white flex items-center gap-2">
              <span>Instant Test Simulator</span>
              <span className="badge badge-applied text-xs px-2 py-0.5">Try Right Now</span>
            </h2>
            <p className="text-xs text-slate-400 mt-0.5">
              Test automated ingestion and updating without waiting for live emails. Click a preset below to see it parse and populate your database instantly!
            </p>
          </div>

          <div className="flex flex-wrap gap-2">
            <button
              type="button"
              disabled={simulating}
              onClick={() => handleSimulate(
                'jobs-listings@linkedin.com',
                'You applied to Backend Engineer at Netflix',
                'Your application has been received by Netflix via LinkedIn Easy Apply.'
              )}
              className="btn btn-secondary text-xs px-3 py-1.5 hover:border-sky-500 hover:text-sky-400 transition-colors"
            >
              + LinkedIn Preset
            </button>
            <button
              type="button"
              disabled={simulating}
              onClick={() => handleSimulate(
                'jobsearch@naukri.com',
                'Application sent for Full Stack Developer at Swiggy',
                'Your application has been successfully forwarded to Swiggy recruiter.'
              )}
              className="btn btn-secondary text-xs px-3 py-1.5 hover:border-emerald-500 hover:text-emerald-400 transition-colors"
            >
              + Naukri Preset
            </button>
            <button
              type="button"
              disabled={simulating}
              onClick={() => handleSimulate(
                'messages-noreply@linkedin.com',
                'Invitation to interview from Netflix for Backend Engineer',
                'Recruiter at Netflix would like to schedule your Technical Interview.'
              )}
              className="btn btn-secondary text-xs px-3 py-1.5 hover:border-purple-500 hover:text-purple-400 transition-colors"
            >
              + Interview Update Preset
            </button>
          </div>
        </div>

        {/* Custom Email Ingestion Input */}
        <div className="p-4 rounded-xl bg-slate-950/60 border border-slate-800 space-y-4">
          <div className="text-xs font-semibold text-slate-300">Custom Email Tester:</div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-[11px] text-slate-400 mb-1">Sender Email</label>
              <input
                type="text"
                value={simSender}
                onChange={(e) => setSimSender(e.target.value)}
                className="input w-full text-xs"
              />
            </div>
            <div>
              <label className="block text-[11px] text-slate-400 mb-1">Email Subject Line</label>
              <input
                type="text"
                value={simSubject}
                onChange={(e) => setSimSubject(e.target.value)}
                className="input w-full text-xs"
              />
            </div>
          </div>
          <div>
            <label className="block text-[11px] text-slate-400 mb-1">Email Body Content</label>
            <textarea
              rows={2}
              value={simBody}
              onChange={(e) => setSimBody(e.target.value)}
              className="input w-full text-xs"
            />
          </div>
          <div className="flex justify-end">
            <button
              type="button"
              disabled={simulating || !simSubject}
              onClick={() => handleSimulate(simSender, simSubject, simBody)}
              className="btn btn-primary text-xs px-4 py-2"
            >
              {simulating ? 'Parsing...' : 'Parse & Import Email'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
