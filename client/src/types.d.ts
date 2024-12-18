type LogType = "info" | "warn";
type FloatType = "left" | "right";

interface Window {
  send: (name: string, msg: string) => void;
  receive: (name: string, msg: string) => void;
  log: (type: LogType, msg: string) => void;
  clear: () => void;
  setInputDisabled: (value: boolean) => void;
  setInputVisible: (value: boolean) => void;
  setPause: (value: boolean) => void;
}

interface ILogItem {
  type: "log";
  msg: string;
  logType: LogType;
}

interface IMessageItem {
  type: "message";
  msg: string;
  name: string;
  float: FloatType;
}

declare const pywebview: {
  api: {
    sendMessage?: (msg: string) => void;
    cancel: () => void;
    pause: () => void;
  };
};
